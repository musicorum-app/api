import * as Yup from 'yup'
import { themes } from '~collages/themes/themes'
import { ThemeName, ThemeGenerationPayloads } from '~collages/types/themes'

const themesValidations = Object.values(themes).map((t) => t.validationSchema)
const themeNames = Object.keys(themes) as ThemeName[]

export const collagePayloadValidation = Yup.object({
  user: Yup.string().min(2).max(16).required(),

  theme: Yup.string().oneOf(themeNames).required(),

  story: Yup.boolean().default(false),

  language: Yup.string().oneOf(['en-US']).default('en-US'),

  hide_username: Yup.boolean().default(false),

  options: Yup.object().required()
})
