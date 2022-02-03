import { Injectable } from '@nestjs/common'
import * as Joi from 'joi'
import {
  entitiesResolverJoi,
  periodResolverJoi,
} from 'src/services/validation/common.joi'
import { CollageRequest } from 'src/services/collages/collages.interface'
import { IGridTheme, Theme } from '../theme.interface'
import { LastfmService } from 'src/services/api/lastfm/lastfm.service'
import { ConfigService } from '@nestjs/config'
import { Entity } from 'src/constants'

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
  ) {}

  async handleDate({
    user,
    options,
  }: CollageRequest<IGridTheme>): Promise<Record<string, any>> {
    let tiles = [] as ITile[]
    const limit = options.columns * options.rows

    if (options.entity === Entity.ALBUM) {
      const { fromWeekly, items } = await this.lastfmService.getAlbumsChart(
        user,
        options.period,
        limit,
      )

      tiles = items.map((album) => ({
        name: album.name,
        secondary: album.artist,
        image: album.image,
        scrobbles: album.playCount,
      }))
    }

    return {
      rows: options.rows,
      columns: options.columns,
      show_names: options.show_names,
      show_playcount: options.show_playcount,
      tile_size: this.configService.get<string>('themes.grid.tile_size'),
      tiles,
      style: options.style,
    }
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
        .required(),
    })
  }
}
