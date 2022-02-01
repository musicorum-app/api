import { CACHE_MANAGER, Inject, Injectable } from '@nestjs/common'
import { Application } from '@prisma/client'
import { Cache } from 'cache-manager'
import { PrismaService } from 'src/database/prisma.service'

@Injectable()
export class ApplicationService {
  constructor(
    private prisma: PrismaService,
    @Inject(CACHE_MANAGER) private cacheManager: Cache,
  ) {}

  async getByKey(key: string): Promise<Application | null> {
    const cached = await this.cacheManager.get(`app:by_key:${key}`)
    if (cached) return cached as Application

    const app = await this.prisma.application.findUnique({
      where: {
        key,
      },
    })

    if (app && app.id) this.cacheManager.set(`app:by_key:${key}`, app)

    return app
  }
}
