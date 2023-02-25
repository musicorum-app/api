import * as Yup from 'yup'
import { themes } from '~collages/themes/themes.js'
import { ThemeName } from '~collages/types/themes.js'

const themesValidations = Object.values(themes).map((t) => t.validationSchema)

export const collagePayloadValidation = Yup.object({
  user: Yup.string().min(2).max(16).required(),

  theme: Yup.string().oneOf(Object.keys(themes)).required(),

  story: Yup.boolean().default(false),

  language: Yup.string().oneOf(['en-US']).default('en-US'),

  hide_username: Yup.boolean().default(false),

  options: Yup.object()
    .required()
    .test({
      message: 'Invalid options for this theme',
      test: (value, context) => {
        const theme = context.parent.theme as string
        if (!Object.keys(themes).includes(theme)) {
          return new Yup.ValidationError('Invalid theme')
        }

        const themeValidation = themes[theme as ThemeName]
        try {
          themeValidation.validationSchema.validateSync(value)
          return true
        } catch (error) {
          if (error instanceof Yup.ValidationError) {
            error.message = 'Invalid theme options: ' + error.message
          }

          throw error
        }
      }
    })
})
