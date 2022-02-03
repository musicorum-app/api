import { HttpException, HttpStatus, Injectable } from '@nestjs/common'
import { nanoid } from 'nanoid'
import { performance } from 'perf_hooks'
import { PrismaService } from 'src/database/prisma.service'
import { GridTheme } from 'src/themes/themes/grid.theme'
import { LastfmService } from '../api/lastfm/lastfm.service'
import { WorkersService } from '../workers/workers.service'
import { CollageRequest } from './collages.interface'
import { InjectSentry, SentryService } from '@ntegral/nestjs-sentry'
import { Transaction } from '@sentry/types'
import * as Sentry from '@sentry/node'

@Injectable()
export class CollagesService {
  private themesInstances = {
    grid: this.gridTheme,
  }

  constructor(
    private workersService: WorkersService,
    private lastfmService: LastfmService,
    private prismaService: PrismaService,
    @InjectSentry() private readonly sentry: SentryService,
    private gridTheme: GridTheme,
  ) {}

  async generateCollage(data: CollageRequest, appId?: string) {
    const start = performance.now()
    const worker = this.workersService.getWorkerForTheme(data.theme)
    if (!worker)
      throw new HttpException(
        'No available worker for that theme',
        HttpStatus.SERVICE_UNAVAILABLE,
      )

    const theme = this.themesInstances[data.theme]
    const id = nanoid(24)

    const transaction = await new Promise<Transaction>((resolve) => {
      this.sentry.instance().configureScope((scope) => {
        scope.setTransactionName(`Collage generation > ${data.theme}`)
        resolve(scope.getTransaction())
      })
    })

    this.sentry.instance().setContext('Image generation', {
      id,
      theme: data.theme,
      worker: worker.name,
      worker_version: worker.version,
    })

    try {
      this.sentry.instance().addBreadcrumb({
        category: 'generation.worker.data',
        level: Sentry.Severity.Info,
        message: 'Handling worker data',
      })
      const workerData = await theme.handleDate(data)
      this.sentry.instance().addBreadcrumb({
        category: 'generation.worker.data',
        level: Sentry.Severity.Info,
        message: 'Worker data handled',
      })

      let user = null
      if (theme.requiresUserData) {
        user = await this.lastfmService.userGetInfo(data.user)
        this.sentry.instance().addBreadcrumb({
          category: 'generation.worker.user',
          level: Sentry.Severity.Info,
          message: 'Getting user information from ' + data.user,
        })
        if (!user)
          throw new HttpException('User not found', HttpStatus.BAD_REQUEST)
      }

      this.sentry.instance().addBreadcrumb({
        category: 'generation.worker.generate',
        level: Sentry.Severity.Info,
        message: 'Starting generation',
      })

      const result = await worker.generate(
        {
          id,
          user,
          theme: data.theme,
          story: data.story,
          hide_username: data.hide_username,
          data: workerData,
        },
        transaction.traceId,
      )

      this.sentry.instance().addBreadcrumb({
        category: 'generation.worker.generate',
        level: Sentry.Severity.Info,
        message: 'Generation done',
      })

      await this.prismaService.collage.create({
        data: {
          id,
          success: true,
          app_id: appId,
          theme: data.theme,
          file: result.file,
          render_duration: result.duration,
          total_duration: performance.now() - start,
        },
      })

      return {
        id,
        ...result,
        trace_id: transaction.traceId,
      }
    } catch (error) {
      console.error(error)
      throw error
    } finally {
    }
  }
}
