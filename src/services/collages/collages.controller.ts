import { Body, Controller, Post, UseGuards } from '@nestjs/common'
import { nanoid } from 'nanoid'
import { RequestApplication } from 'src/decorators/application.decorator'
import { ValidationException } from 'src/exceptions/validation.exception'
import { ApiKeyGuard } from 'src/guards/api-key.guard'
import { ValidationService } from 'src/services/validation/validation.service'
import { gridWorkerDataValidation } from '../validation/grid.joi'
import { WorkersService } from '../workers/workers.service'
import { CollagesService } from './collages.service'

@Controller('collages')
export class CollagesController {
  constructor(
    private validationService: ValidationService,
    private collagesService: CollagesService,
    private workersService: WorkersService
  ) {}

  @UseGuards(ApiKeyGuard)
  @Post('generate')
  async generate(@Body() body: any, @RequestApplication() application) {
    const data = this.validationService.validateGeneratePayload(body)

    return this.collagesService.generateCollage(data, application.id)
  }

  @UseGuards(ApiKeyGuard)
  @Post('generate-rewind')
  async generateRewind(
    @Body() body: unknown,
    @RequestApplication() application
  ) {
    const { error, value } = gridWorkerDataValidation.validate(body)

    if (error) throw new ValidationException(error)

    const id = nanoid(24)

    const themeData = {
      tiles: value.tiles,
      rows: 10,
      columns: 10,
      show_names: false,
      show_playcount: false,
      tile_size: 200,
      style: 'DEFAULT'
    }

    const workerData = {
      id,
      user: {},
      theme: 'grid',
      story: false,
      hide_username: true,
      data: themeData
    }

    const result = await this.workersService
      .getWorkerForTheme('grid')
      ?.generate(workerData)

    if (result && result.file) {
      return {
        ...result,
        id,
        url: process.env.RESULT_URL + result.file
      }
    } else {
      console.warn(result)
      throw new Error('Error while rendering')
    }
  }
}
