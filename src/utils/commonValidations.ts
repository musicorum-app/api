import { Entity, Period } from '~/common.js'
import * as Yup from 'yup'

export const entityValidation = Yup.string().oneOf(Object.values(Entity))

export const periodValidation = Yup.string().oneOf(Object.values(Period))
