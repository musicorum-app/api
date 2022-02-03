import { Request } from 'express'
import { Application } from '@prisma/client'

export interface RequestWithApplication extends Request {
  application: Application
}

export interface WorkerGenerationResponse {
  id: string
  duration: number
  file: string
}
