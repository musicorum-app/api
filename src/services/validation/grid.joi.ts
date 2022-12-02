import * as Joi from 'joi'

export const gridWorkerDataValidation = Joi.object({
  tiles: Joi.array()
    .items(
      Joi.object({
        name: Joi.string().optional().allow(null),
        image: Joi.string().optional().allow(null)
      })
    )
    .required()
})
