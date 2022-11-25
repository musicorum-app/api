import { DuotoneTheme } from './themes/duotone.theme.js'
import { GridTheme } from './themes/grid.theme.js'

export const themes = {
  grid: GridTheme,
  duotone: DuotoneTheme
} as const

export const availableThemes = Object.keys(themes)
export type ThemeType = keyof typeof themes
