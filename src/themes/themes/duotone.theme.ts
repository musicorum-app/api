import { Injectable } from '@nestjs/common'
import { ConfigService } from '@nestjs/config'
import { InjectSentry, SentryService } from '@ntegral/nestjs-sentry'
import Joi from 'joi'
import { Entity, Period } from 'src/constants'
import { LastfmService } from 'src/services/api/lastfm/lastfm.service'
import { CollageRequest } from 'src/services/collages/collages.interface'
import {
  getPrefferedImageResource,
  ResourcesService
} from 'src/services/resources/resources.service'
import {
  IHaveImageResources,
  ImageSize,
  Nullable
} from 'src/services/resources/resources.type'
import {
  entitiesResolverJoi,
  periodResolverJoi
} from 'src/services/validation/common.joi'
import { IDuotoneTheme, Theme } from '../theme.interface'

type Palette = [string, string]

interface DuotoneWorkerData {
  items: {
    image?: string
    name: string
    secondary?: string
  }[]
  title: string
  palette: string
}

@Injectable()
export class DuotoneTheme implements Theme {
  public name = 'duotone'
  public requiresUserData = false
  private palettes: Record<string, Palette>

  constructor(
    private lastfmService: LastfmService,
    private configService: ConfigService,
    private resourcesService: ResourcesService,
    @InjectSentry() private sentryService: SentryService
  ) {
    const palettes = this.configService.get<typeof this.palettes>(
      'themes.duotone.palettes'
    )
    if (!palettes) throw new Error('Could not initialize palettes')
    this.palettes = palettes
  }

  async handleDate({
    user,
    options,
    story
  }: CollageRequest<IDuotoneTheme>): Promise<DuotoneWorkerData> {
    let items: DuotoneWorkerData['items']

    const limit = story ? 8 : 6
    const scrobbleCount = await this.lastfmService.getScrobbleCount(
      user,
      options.period
    )

    if (options.entity === Entity.ALBUM) {
      const chart = await this.lastfmService.getAlbumsChart(
        user,
        options.period,
        limit
      )

      items = chart.items.map((album) => ({
        name: album.name,
        secondary: album.artist,
        image: album.image
      }))
    } else if (options.entity === Entity.ARTIST) {
      const chart = await this.lastfmService.getArtistsChart(
        user,
        options.period,
        limit
      )

      // const endResources = this.startResourcesSentry(Entity.ARTIST)
      const artists = await this.resourcesService.findArtists(
        chart.items.map((a) => a.name)
      )
      // endResources()

      items = chart.items.map((artist, index) => ({
        name: artist.name,
        image: this.resolveImage(artists, index) || '' // @todo: default image
      }))
    } else if (options.entity === Entity.TRACK) {
      const chart = await this.lastfmService.getTracksChart(
        user,
        options.period,
        limit
      )

      // const endResources = this.startResourcesSentry(Entity.TRACK)
      const tracks = await this.resourcesService.findTracks(
        chart.items.map((t) => ({
          name: t.name,
          artist: t.artist
        }))
      )
      // endResources()

      items = chart.items.map((track, index) => ({
        name: track.name,
        secondary: track.artist,
        image: this.resolveImage(tracks, index) || ''
      }))
    } else {
      throw new Error('Wrong entity')
    }

    // let tileSize = this.configService.get<string>('themes.grid.tile_size')

    // if (limit >= 250) {
    //   tileSize = this.configService.get<string>('themes.grid.tile_size_250')
    // } else if (limit >= 100) {
    //   tileSize = this.configService.get<string>('themes.grid.tile_size_100')
    // } else if (limit >= 25) {
    //   tileSize = this.configService.get<string>('themes.grid.tile_size_25')
    // }

    // console.log(tileSize)

    return {
      items,
      palette: options.palette,
      title: `${user} TOP ${options.entity}S`.toUpperCase()
    }
  }

  private resolveImage(
    resourcesList: Nullable<IHaveImageResources>[],
    index: number
  ) {
    const resource = resourcesList[index]
    if (!resource) return null
    const imageResource = getPrefferedImageResource(resource)
    const image =
      imageResource?.images.find((i) => i.size === ImageSize.Large) ||
      imageResource?.images[0]
    return image?.url ?? null
  }

  createValidationSchema(): Joi.ObjectSchema {
    return Joi.object({
      period: periodResolverJoi,

      entity: entitiesResolverJoi,

      style: Joi.string()
        .valid(...Object.keys(this.palettes))
        .required()
    })
  }
}
