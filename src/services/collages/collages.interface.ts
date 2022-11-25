import { ThemeOptions } from 'src/themes/theme.interface.js'
import { ThemeType } from 'src/themes/themes.js'

export interface CollageRequest<TO = ThemeOptions> {
  user: string
  theme: ThemeType
  language: string
  options: TO
  hide_username: boolean
  story: boolean
}
