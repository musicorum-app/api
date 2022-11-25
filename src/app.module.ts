import {
  CacheModule,
  MiddlewareConsumer,
  Module,
  NestModule,
  RequestMethod
} from '@nestjs/common'
import { ConfigModule } from '@nestjs/config'
import { MusicorumController } from './app.controller.js'
import { MusicorumService } from './app.service.js'
import { WorkersService } from './services/workers/workers.service.js'
import { WorkersController } from './services/workers/workers.controller.js'
import { PrismaService } from './database/prisma.service.js'
import { ApplicationService } from './services/application/application.service.js'
import { SentryModule, SentryService } from '@ntegral/nestjs-sentry'
import { APP_FILTER, HttpAdapterHost } from '@nestjs/core'
import { HttpExceptionFilter } from './filters/http-exception.filter.js'
import { CollagesController } from './services/collages/collages.controller.js'
import * as redisStore from 'cache-manager-redis-store'
import { themes } from './themes/themes.js'
import { ValidationService } from './services/validation/validation.service.js'
import { LastfmService } from './services/api/lastfm/lastfm.service.js'
import { CollagesService } from './services/collages/collages.service.js'
import configuration from './configuration.js'
import * as Sentry from '@sentry/node'
import * as Tracing from '@sentry/tracing'
import { ResourcesService } from './services/resources/resources.service.js'
import { ResourcesController } from './services/resources/resources.controller.js'

console.log(process.env.NODE_ENV)
@Module({
  imports: [
    ConfigModule.forRoot({
      load: [configuration]
    }),
    SentryModule.forRootAsync({
      inject: [HttpAdapterHost],
      useFactory: async (adapter: HttpAdapterHost) => ({
        dsn: process.env.SENTRY_DSN,
        environment: process.env.NODE_ENV,
        integrations: [
          new Sentry.Integrations.Http({ tracing: true }),
          new Tracing.Integrations.Express({
            app: adapter.httpAdapter.getInstance()
          })
        ],
        tracesSampleRate: process.env.NODE_ENV === 'production' ? 0.5 : 1.0,
        debug: process.env.NODE_ENV !== 'production'
      })
    }),
    CacheModule.register({
      store: redisStore,
      url: process.env.REDIS_URL,
      ttl: 60 * 60 // 1 hour
    })
  ],
  controllers: [
    MusicorumController,
    WorkersController,
    CollagesController,
    ResourcesController
  ],
  providers: [
    MusicorumService,
    WorkersService,
    PrismaService,
    ApplicationService,
    {
      provide: APP_FILTER,
      useClass: HttpExceptionFilter
    },
    ...Object.values(themes),
    ValidationService,
    LastfmService,
    CollagesService,
    ResourcesService
  ]
})
export class MusicorumModule implements NestModule {
  constructor(private sentry: SentryService) {}
  configure(consumer: MiddlewareConsumer) {
    consumer
      .apply(this.sentry.instance().Handlers.requestHandler())
      .forRoutes({
        path: '*',
        method: RequestMethod.ALL
      })
      .apply(this.sentry.instance().Handlers.tracingHandler())
      .forRoutes({
        path: '*',
        method: RequestMethod.ALL
      })
  }
}
