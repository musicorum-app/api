import { Injectable } from '@nestjs/common'
import * as Joi from 'joi'
import {
  entitiesResolverJoi,
  periodResolverJoi,
} from 'src/services/validation/common.joi'
import { CollageRequest } from 'src/services/collages/collages.interface'
import { IGridTheme, Theme } from '../theme.interface'
import { LastfmService } from 'src/services/api/lastfm/lastfm.service'

@Injectable()
export class GridTheme implements Theme {
  public name = 'grid'
  public requiresUserData = false

  constructor(private lastfmService: LastfmService) {}

  async handleDate({
    user,
    options,
  }: CollageRequest<IGridTheme>): Promise<Record<string, any>> {
    return {
      rows: options.rows,
      columns: options.columns,
      show_names: options.show_names,
      show_playcount: options.show_playcount,
      tile_size: 200,
      tiles: [
        {
          image:
            'https://i.scdn.co/image/ab67616d0000b2736040effba89b9b00a6f6743a',
          name: 'Replay',
          sub: 'Lady Gaga',
        },
        {
          image:
            'https://i.scdn.co/image/ab67616d0000b2733899712512f50a8d9e01e951',
          name: 'Play Date',
          sub: 'Melanie Martinez',
        },
      ],
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
