import { Injectable } from '@nestjs/common'
import * as Joi from 'joi'
import { ValidationException } from 'src/exceptions/validation.exception'
import { CollageRequest } from 'src/services/collages/collages.interface'
import { Theme } from 'src/themes/theme.interface'
import { availableThemes, ThemeType } from 'src/themes/themes'
import { DuotoneTheme } from 'src/themes/themes/duotone.theme'
import { GridTheme } from 'src/themes/themes/grid.theme'

@Injectable()
export class ValidationService {
  private themes: Theme[]
  private themesJoi = {} as Record<ThemeType, Joi.ObjectSchema>
  private generateJoi = this.createGenerateJoi()

  constructor(
    private gridTheme: GridTheme,
    private duotoneTheme: DuotoneTheme
  ) {
    this.themes = [this.gridTheme, this.duotoneTheme]
    for (const theme of this.themes) {
      this.themesJoi[theme.name] = theme.createValidationSchema()
    }
  }

  public validateGeneratePayload(data: Record<string, any>): CollageRequest {
    const validation = this.generateJoi.validate(data)

    if (validation.error) throw new ValidationException(validation.error)

    return validation.value as CollageRequest
  }

  private createGenerateJoi() {
    const themeOptionsJoi = (
      value: Record<string, any>,
      helpers: Joi.CustomHelpers
    ) => {
      const { theme } = helpers.state.ancestors[0]
      Joi.assert(value, this.themesJoi[theme])

      return value
    }

    return Joi.object({
      user: Joi.string().min(2).max(15).required(),

      theme: Joi.string()
        .valid(...availableThemes)
        .required(),

      story: Joi.bool().required(),

      language: Joi.string().valid(...['en-US']),

      hide_username: Joi.bool().required(),

      options: Joi.object().custom(themeOptionsJoi)
    })
  }
}
