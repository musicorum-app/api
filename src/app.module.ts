import {
  CacheModule,
  MiddlewareConsumer,
  Module,
  NestModule,
  RequestMethod
} from '@nestjs/common'
import { ConfigModule, ConfigService } from '@nestjs/config'
import { MusicorumController } from './app.controller'
import { MusicorumService } from './app.service'
import { WorkersService } from './services/workers/workers.service'
import { WorkersController } from './services/workers/workers.controller'
import { PrismaService } from './database/prisma.service'
import { ApplicationService } from './services/application/application.service'
import { SentryModule, SentryService } from '@ntegral/nestjs-sentry'
import { APP_FILTER, HttpAdapterHost } from '@nestjs/core'
import { HttpExceptionFilter } from './filters/http-exception.filter'
import type { RedisClientOptions, RedisScripts } from 'redis'
import { CollagesController } from './services/collages/collages.controller'
import * as redisStore from 'cache-manager-redis-store'
import { themes } from './themes/themes'
import { ValidationService } from './services/validation/validation.service'
import { LastfmService } from './services/api/lastfm/lastfm.service'
import { CollagesService } from './services/collages/collages.service'
import configuration from './configuration'
import * as Sentry from '@sentry/node'
import * as Tracing from '@sentry/tracing'
import { ExpressAdapter } from '@nestjs/platform-express'
import { ResourcesService } from './services/resources/resources.service'
import { ResourcesController } from './services/resources/resources.controller'

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
        tracesSampleRate: 1.0
      })
    }),
    CacheModule.register<RedisClientOptions<never, RedisScripts>>({
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
