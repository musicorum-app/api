import { CacheModule, Module } from '@nestjs/common'
import { ConfigModule } from '@nestjs/config'
import { MusicorumController } from './app.controller'
import { MusicorumService } from './app.service'
import { WorkersService } from './services/workers/workers.service'
import { WorkersController } from './services/workers/workers.controller'
import { PrismaService } from './database/prisma.service'
import { ApplicationService } from './services/application/application.service'
import { SentryModule } from '@ntegral/nestjs-sentry'
import { APP_FILTER } from '@nestjs/core'
import { HttpExceptionFilter } from './filters/http-exception.filter'
import type { RedisClientOptions, RedisScripts } from 'redis'
import { CollagesController } from './services/collages/collages.controller'
import * as redisStore from 'cache-manager-redis-store'
import { themes } from './themes/themes'
import { ValidationService } from './services/validation/validation.service'
import { LastfmService } from './services/api/lastfm/lastfm.service'
import { CollagesService } from './services/collages/collages.service'

@Module({
  imports: [
    ConfigModule.forRoot(),
    SentryModule.forRoot({
      dsn: process.env.SENTRY_DSN,
      debug: true,
    }),
    CacheModule.register<RedisClientOptions<never, RedisScripts>>({
      store: redisStore,
      url: process.env.REDIS_URL,
      ttl: 60 * 60, // 1 hour
    }),
  ],
  controllers: [MusicorumController, WorkersController, CollagesController],
  providers: [
    MusicorumService,
    WorkersService,
    PrismaService,
    ApplicationService,
    {
      provide: APP_FILTER,
      useClass: HttpExceptionFilter,
    },
    ...Object.values(themes),
    ValidationService,
    LastfmService,
    CollagesService,
  ],
})
export class MusicorumModule {}
