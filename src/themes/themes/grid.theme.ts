import { Injectable } from '@nestjs/common'
import * as Joi from 'joi'
import {
  entitiesResolverJoi,
  periodResolverJoi
} from 'src/services/validation/common.joi'
import { CollageRequest } from 'src/services/collages/collages.interface'
import { IGridTheme, Theme } from '../theme.interface'
import {
  getPrefferedImageResource,
  ResourcesService
} from '../../services/resources/resources.service'
import { LastfmService } from 'src/services/api/lastfm/lastfm.service'
import { ConfigService } from '@nestjs/config'
import { Entity } from 'src/constants'
import {
  IHaveImageResources,
  ImageSize,
  Nullable
} from 'src/services/resources/resources.type'
import { InjectSentry, SentryService } from '@ntegral/nestjs-sentry'
import { Severity } from '@sentry/node'

interface ITile {
  image: string
  name: string
  secondary?: string
  scrobbles?: number
}
@Injectable()
export class GridTheme implements Theme {
  public name = 'grid'
  public requiresUserData = false

  constructor(
    private lastfmService: LastfmService,
    private configService: ConfigService,
    private resourcesService: ResourcesService,
    @InjectSentry() private sentryService: SentryService
  ) {}

  async handleDate({
    user,
    options
  }: CollageRequest<IGridTheme>): Promise<Record<string, any>> {
    let tiles = [] as ITile[]
    const limit = options.columns * options.rows

    if (options.entity === Entity.ALBUM) {
      const { items } = await this.lastfmService.getAlbumsChart(
        user,
        options.period,
        limit
      )

      tiles = items.map((album) => ({
        name: album.name,
        secondary: album.artist,
        image: album.image,
        scrobbles: album.playCount
      }))
    } else if (options.entity === Entity.ARTIST) {
      const { items } = await this.lastfmService.getArtistsChart(
        user,
        options.period,
        limit
      )

      const endResources = this.startResourcesSentry(Entity.ARTIST)
      const artists = await this.resourcesService.findArtists(
        items.map((a) => a.name)
      )
      endResources()

      tiles = items.map((artist, index) => ({
        name: artist.name,
        scrobbles: artist.playCount,
        image: this.resolveImage(artists, index) || '' // @todo: default image
      }))
    } else if (options.entity === Entity.TRACK) {
      const { items } = await this.lastfmService.getTracksChart(
        user,
        options.period,
        limit
      )

      const endResources = this.startResourcesSentry(Entity.TRACK)
      const tracks = await this.resourcesService.findTracks(
        items.map((t) => ({
          name: t.name,
          artist: t.artist
        }))
      )
      endResources()

      tiles = items.map((track, index) => ({
        name: track.name,
        secondary: track.artist,
        scrobbles: track.playCount,
        image: this.resolveImage(tracks, index) || ''
      }))
    }

    let tileSize = this.configService.get<string>('themes.grid.tile_size')

    if (limit >= 250) {
      tileSize = this.configService.get<string>('themes.grid.tile_size_250')
    } else if (limit >= 100) {
      tileSize = this.configService.get<string>('themes.grid.tile_size_100')
    } else if (limit >= 25) {
      tileSize = this.configService.get<string>('themes.grid.tile_size_25')
    }

    console.log(tileSize)

    return {
      rows: options.rows,
      columns: options.columns,
      show_names: options.show_names,
      show_playcount: options.show_playcount,
      tile_size: tileSize,
      tiles,
      style: options.style
    }
  }

  private startResourcesSentry(entity: Entity) {
    this.sentryService.instance().addBreadcrumb({
      category: 'generation.resources',
      level: Severity.Info,
      message: 'Starting resoures',
      data: {
        entity
      }
    })

    return () => {
      this.sentryService.instance().addBreadcrumb({
        category: 'generation.resources',
        level: Severity.Info,
        message: 'Ended resoures'
      })
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
      rows: Joi.number().min(3).max(20).required(),

      columns: Joi.number().min(3).max(20).required(),

      show_names: Joi.bool().required(),

      show_playcount: Joi.bool().required(),

      period: periodResolverJoi,

      entity: entitiesResolverJoi,

      style: Joi.string()
        .valid(...['DEFAULT', 'CAPTION', 'SHADOW'])
        .required()
    })
  }
}
