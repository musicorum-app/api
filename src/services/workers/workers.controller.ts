import { Controller, Get, UseGuards } from '@nestjs/common'
import { ApiKeyGuard } from 'src/guards/api-key.guard.js'
import { Worker } from './worker.js'
import { WorkersService } from './workers.service.js'

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
