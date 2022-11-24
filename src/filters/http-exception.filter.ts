import {
  ArgumentsHost,
  Catch,
  ExceptionFilter,
  HttpException,
  Injectable,
  Logger
} from '@nestjs/common'
import { InjectSentry, SentryService } from '@ntegral/nestjs-sentry'
import { Response } from 'express'

@Catch()
@Injectable()
export class HttpExceptionFilter implements ExceptionFilter {
  private logger = new Logger('ExceptionFilter')

  constructor(@InjectSentry() private readonly sentryService: SentryService) {}

  async catch(exception: any, host: ArgumentsHost) {
    const ctx = host.switchToHttp()
    const response = ctx.getResponse<Response>()

    if (exception instanceof HttpException) {
      return this.catchHttpException(exception, response)
    }

    const eventId = this.sentryService.instance().captureException(exception)
    this.logger.error(exception, exception.stack)

    response.status(500).json({
      status: 500,
      error: 'Internal error',
      message: 'Internal error',
      event_id: eventId
    })
  }

  private catchHttpException(exception: HttpException, response: Response) {
    const status = exception.getStatus()
    const exceptionResponse = exception.getResponse() as Record<string, any>

    delete exceptionResponse.statusCode

    response.status(status).json({
      status,
      error:
        exceptionResponse.error || typeof exceptionResponse === 'string'
          ? exceptionResponse
          : 'Internal error',
      message: exceptionResponse.message || undefined,
      ...(typeof exceptionResponse === 'object' ? exceptionResponse : {})
    })
  }
}
