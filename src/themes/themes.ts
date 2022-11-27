import { DuotoneTheme } from './themes/duotone.theme'
import { GridTheme } from './themes/grid.theme'

export const themes = {
  grid: GridTheme,
  duotone: DuotoneTheme
} as const

export const availableThemes = Object.keys(themes)
export type ThemeType = keyof typeof themes
