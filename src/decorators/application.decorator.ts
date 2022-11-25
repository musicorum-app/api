import { createParamDecorator, ExecutionContext } from '@nestjs/common'
import { RequestWithApplication } from 'src/types/common.js'

export const RequestApplication = createParamDecorator(
  (_: unknown, context: ExecutionContext) => {
    const req = context.switchToHttp().getRequest() as RequestWithApplication
    return req.application
  }
)
