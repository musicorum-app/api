import { Controller, Get, UseGuards } from '@nestjs/common'
import { ApiKeyGuard } from 'src/guards/api-key.guard'
import { Worker } from './worker'
import { WorkersService } from './workers.service'

@Controller('workers')
export class WorkersController {
  constructor(private workersProvider: WorkersService) {}

  @Get()
  @UseGuards(ApiKeyGuard)
  getWorkers() {
    return {
      workers: [...this.workersProvider.workers.values()].map(
        (worker: Worker) => ({
          name: worker.name,
          engine: worker.engine,
          version: worker.version,
          scheme: worker.scheme,
          themes: worker.themes,
          available: worker.available
        })
      )
    }
  }
}
