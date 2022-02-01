import { Request } from 'express'
import { Application } from '@prisma/client'

export interface RequestWithApplication extends Request {
  application: Application
}
