import { Body, Controller, Post, UseGuards } from '@nestjs/common'
import { RequestApplication } from 'src/decorators/application.decorator.js'
import { ApiKeyGuard } from 'src/guards/api-key.guard.js'
import { ValidationService } from 'src/services/validation/validation.service.js'
import { CollagesService } from './collages.service.js'

@Controller('collages')
export class CollagesController {
  constructor(
    private validationService: ValidationService,
    private collagesService: CollagesService
  ) {}

  @UseGuards(ApiKeyGuard)
  @Post('generate')
  async generate(@Body() body: any, @RequestApplication() application) {
    const data = this.validationService.validateGeneratePayload(body)

    return this.collagesService.generateCollage(data, application.id)
  }
}
