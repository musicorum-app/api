import {
  CanActivate,
  ExecutionContext,
  Injectable,
  UnauthorizedException,
} from '@nestjs/common'
import { ApplicationService } from 'src/services/application/application.service'
import { RequestWithApplication } from 'src/types/common'

@Injectable()
export class ApiKeyGuard implements CanActivate {
  constructor(private applicationService: ApplicationService) {}

  async canActivate(context: ExecutionContext): Promise<boolean> {
    const request = context
      .switchToHttp()
      .getRequest() as RequestWithApplication

    const authorization = request.headers.authorization

    const key = authorization?.split(' ')[1] || request.query['api_key']

    if (!key || typeof key !== 'string')
      throw new UnauthorizedException('Api key not present on request')

    const app = await this.applicationService.getByKey(key)
    if (!app) throw new UnauthorizedException('Api key not found or invalid')

    request.application = app

    return true
  }
}
