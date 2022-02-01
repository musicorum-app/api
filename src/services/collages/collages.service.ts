import { HttpException, HttpStatus, Injectable } from '@nestjs/common'
import { nanoid } from 'nanoid'
import { GridTheme } from 'src/themes/themes/grid.theme'
import { LastfmService } from '../api/lastfm/lastfm.service'
import { WorkersService } from '../workers/workers.service'
import { CollageRequest } from './collages.interface'

@Injectable()
export class CollagesService {
  private themesInstances = {
    grid: this.gridTheme,
  }

  constructor(
    private workersService: WorkersService,
    private gridTheme: GridTheme,
    private lastfmService: LastfmService,
  ) {}

  async generateCollage(data: CollageRequest) {
    const worker = this.workersService.getWorkerForTheme(data.theme)
    if (!worker)
      throw new HttpException(
        'No available worker for that theme',
        HttpStatus.SERVICE_UNAVAILABLE,
      )

    const theme = this.themesInstances[data.theme]

    const workerData = await theme.handleDate(data)

    let user = null
    if (theme.requiresUserData) {
      user = await this.lastfmService.userGetInfo(data.user)
      if (!user)
        throw new HttpException('User not found', HttpStatus.BAD_REQUEST)
    }

    const id = nanoid(24)

    const result = await worker.generate({
      id,
      user,
      theme: data.theme,
      story: data.story,
      hide_username: data.hide_username,
      data: workerData,
    })

    return {
      id,
      ...result,
    }
  }
}
