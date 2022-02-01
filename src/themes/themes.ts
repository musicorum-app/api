import { GridTheme } from './themes/grid.theme'

export const themes = {
  grid: GridTheme,
} as const

export const availableThemes = Object.keys(themes)
export type ThemeType = keyof typeof themes
