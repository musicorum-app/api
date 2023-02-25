import { Entity, Period } from 'src/common.js'
import * as Yup from 'yup'
import {
  GridGenerationPayload,
  GridWorkerPayload
} from '~collages/themes/grid.js'

export interface Themes {
  grid: ThemeType<GridGenerationPayload, GridWorkerPayload>
}

type ThemeType<GP = ThemeGenerationPayloads, WP = ThemeWorkerPayloads> = {
  generationPayload: GP
  workerPayload: WP
}

export type ThemeName = keyof Themes

export type ThemeGenerationPayloads = Themes[ThemeName]['generationPayload']
export type ThemeWorkerPayloads = Themes[ThemeName]['workerPayload']

export interface CollagePayload<
  TO extends ThemeGenerationPayloads = ThemeGenerationPayloads
> {
  user: string
  theme: ThemeName
  language?: string
  options: TO
  hide_username: boolean
}

export interface Theme<T extends ThemeType> {
  name: string
  needsUserData: boolean
  validationSchema: Yup.Schema

  handlePayload(
    payload: CollagePayload<T['generationPayload']>
  ): Promise<T['workerPayload']>
}
