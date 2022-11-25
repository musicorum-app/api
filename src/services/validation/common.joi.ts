import { Entity, Period } from '../../constants.js'
import * as Joi from 'joi'

export const entitiesResolverJoi = Joi.string()
  .valid(...Object.values(Entity))
  .required()

export const periodResolverJoi = [
  Joi.string()
    .valid(...Object.values(Period))
    .required() /*,
  Joi.array().items(Joi.number()).length(2).required(),*/
  // Disabling custom periods for now
]
