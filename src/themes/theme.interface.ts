import { CollageRequest } from 'src/services/collages/collages.interface.js'
import { Entity, PeriodResolvable } from 'src/constants.js'
import Joi from 'joi'
import { ThemeType } from './themes.js'

export interface Theme {
  name: string
  requiresUserData: boolean

  handleDate(data: CollageRequest): Promise<Record<string, any>>
  createValidationSchema(): Joi.AnySchema
}

export interface IGridTheme {
  rows: number
  columns: number
  show_names: boolean
  show_playcount: boolean
  period: PeriodResolvable
  entity: Entity
  style: 'DEFAULT' | 'CAPTION' | 'SHADOW'
}

export interface IDuotoneTheme {
  period: PeriodResolvable
  entity: Entity
  palette: string
}

export interface IWorkerGenerationData {
  id: string
  user: {
    username: string
    name?: string
    scrobbles: number
  }
  theme: ThemeType
  story: boolean
  hide_username: boolean
  data: Record<string, any>
}

export type ThemeOptions = IGridTheme | IDuotoneTheme
