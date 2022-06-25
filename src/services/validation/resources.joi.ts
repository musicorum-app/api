import * as Joi from 'joi'

export const artistResourcesPayloadJoi = Joi.object({
  artists: Joi.array().items(Joi.string()).required()
}).required()

export const albumResourcesPayloadJoi = Joi.object({
  albums: Joi.array()
    .items(
      Joi.object({
        name: Joi.string().required(),
        artist: Joi.string().required()
      })
    )
    .required()
}).required()

export const trackResourcesPayloadJoi = Joi.object({
  tracks: Joi.array()
    .items(
      Joi.object({
        name: Joi.string().required(),
        artist: Joi.string(),
        album: Joi.string()
      })
    )
    .required()
}).required()
