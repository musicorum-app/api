import { Injectable, Logger } from '@nestjs/common'
import { ConfigService } from '@nestjs/config'
import { ThemeType } from 'src/themes/themes.js'
import { Worker } from './worker.js'

@Injectable()
export class WorkersService {
  private readonly maxTries = 10
  private readonly logger = new Logger(WorkersService.name)
  public workers = new Set<Worker>()

  constructor(private configService: ConfigService) {
    this.setupWorkers()
  }

  private async setupWorkers() {
    this.logger.log('Starting workers configuration...')
    const urls = this.configService.get<string>('WORKERS')

    if (!urls)
      return this.logger.warn('Stopping configuration. No workers defined')

    for (let url of urls.split(';')) {
      if (url.endsWith('/')) {
        url = url.slice(0, -1)
      }

      const worker = new Worker(url)
      this.workers.add(worker)
      this.handleUrl(worker)
    }
  }

  public getWorkerForTheme(theme: ThemeType): Worker | null {
    for (const worker of this.workers) {
      if (worker.available && worker.themes.includes(theme)) {
        return worker
      }
    }
    return null
  }

  private async handleUrl(worker: Worker, tries = 0) {
    worker
      .setup()
      .then(() => {
        this.logger.log(
          `New worker '${worker.name}' registered at ${worker.url} using '${worker.engine}' ${worker.version}`
        )
      })
      .catch((err) => {
        this.logger.warn(
          `Could not connect to worker at ${worker.url}: '${err}'. Retrying (${tries}/${this.maxTries})...`
        )

        if (tries < this.maxTries) {
          setTimeout(() => {
            this.handleUrl(worker, ++tries)
          }, 5000)
        }
      })
  }
}
