import { Theme, Themes } from '~collages/types/themes.js'
import * as Yup from 'yup'
import { entityValidation, periodValidation } from '@utils/commonValidations.js'
import { Entity, Period } from '~/common.js'
import { lastfmClient } from '@services/lastfm.js'

type GridStyle = 'DEFAULT' | 'CAPTION' | 'SHADOW'
export interface GridGenerationPayload {
  rows: number
  columns: number
  show_names: boolean
  show_playcount: boolean
  padded: boolean
  period: Period
  entity: Entity
  style: GridStyle
}

interface GridTile {
  image: string
  name: string
  secondary?: string
  playcount?: number
}

export interface GridWorkerPayload {
  tiles: GridTile[]
  padded: boolean // padding between tiles
  rows: number
  columns: number
  show_names: boolean
  show_playcount: boolean
  style: GridStyle
  tile_size: number // tile size (width and height)
}

const validationSchema = Yup.object({
  rows: Yup.number().min(3).max(20).required(),
  columns: Yup.number().min(3).max(20).required(),
  show_names: Yup.boolean().default(false),
  show_playcount: Yup.boolean().default(false),
  padded: Yup.boolean().default(false),
  period: periodValidation.required(),
  entity: entityValidation.required(),
  style: Yup.string().oneOf(['DEFAULT', 'CAPTION', 'SHADOW']).required()
})

export const gridTheme: Theme<Themes['grid']> = {
  name: 'grid',
  needsUserData: false,
  validationSchema,

  async handlePayload(payload) {
    let tiles: GridTile[] = []

    const size = payload.options.columns * payload.options.rows

    if (payload.options.entity === Entity.ALBUM) {
      const { albums } = await lastfmClient.user.getTopAlbums(payload.user, {
        limit: size,
        period: payload.options.period as unknown as '7day'
      })

      tiles = albums.map((a) => ({
        name: a.name,
        secondary: a.artist.name,
        image: a.images.at(-1)!.url,
        playcount: a.playCount
      }))
    }

    return {
      rows: payload.options.rows,
      columns: payload.options.columns,
      padded: payload.options.padded,
      show_names: payload.options.show_names,
      show_playcount: payload.options.show_playcount,
      style: payload.options.style,
      tile_size: 300,
      tiles
    }
  }
}
