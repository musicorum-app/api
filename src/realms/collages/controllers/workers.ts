import { workers } from '@services/workers/workersService.js'

export function listWorkersController() {
  return {
    workers: Array.from(workers.values()).map((worker) => ({
      name: worker.name,
      engine: worker.engine,
      version: worker.version,
      themes: worker.themes,
      available: worker.available
    }))
  }
}
