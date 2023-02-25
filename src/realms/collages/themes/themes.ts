import { Theme, ThemeName, Themes } from '~collages/types/themes.js'
import { gridTheme } from './grid.js'

export const themes: Record<ThemeName, Theme<Themes[ThemeName]>> = {
  grid: gridTheme
}
