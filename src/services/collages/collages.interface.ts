import { ThemeOptions } from 'src/themes/theme.interface'
import { ThemeType } from 'src/themes/themes'

export interface CollageRequest<TO = ThemeOptions> {
  user: string
  theme: ThemeType
  language: string
  options: TO
  hide_username: boolean
  story: boolean
}
