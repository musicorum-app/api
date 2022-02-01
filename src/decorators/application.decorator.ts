import { createParamDecorator, ExecutionContext } from '@nestjs/common'
import { RequestWithApplication } from 'src/types/common'

export const RequestApplication = createParamDecorator(
  (_: unknown, context: ExecutionContext) => {
    const req = context.switchToHttp().getRequest() as RequestWithApplication
    return req.application
  },
)
