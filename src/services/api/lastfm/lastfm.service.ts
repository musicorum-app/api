import { CACHE_MANAGER, Inject, Injectable, Logger } from '@nestjs/common'
import axios, { AxiosResponse } from 'axios'
import { Cache } from 'cache-manager'
import { defaultAlbumImage, Period, PeriodResolvable } from 'src/constants'
import { LastfmException } from 'src/exceptions/lastfm.exception'
import {
  LastfmImages,
  LastfmTopAlbumsResponse,
  LastfmUserInfo,
} from './lastfm.types'

@Injectable()
export class LastfmService {
  private logger = new Logger(LastfmService.name)

  constructor(@Inject(CACHE_MANAGER) private cacheService: Cache) {
    if (!process.env.LASTFM_KEY) {
      this.logger.error(`'LASTFM_KEY' environment variable not present.`)
    }
  }

  public async request<D = Record<string, any>>(
    method: string,
    params: Record<string, any>,
  ): Promise<D> {
    this.logger.verbose(`Doing request on last.fm for method '${method}'`)
    try {
      const res = await axios.get('http://ws.audioscrobbler.com/2.0/', {
        params: {
          method,
          format: 'json',
          api_key: process.env.LASTFM_KEY,
          ...params,
        },
      })
      return res.data
    } catch (error) {
      if (
        error.isAxiosError &&
        error.response.data &&
        error.response.data.error
      ) {
        this.logger.warn(
          `Last.fm api error: ${error.response.data.message} (${error.response.data.error})`,
        )
        throw new LastfmException(error.response.data)
      } else {
        throw error
      }
    }
  }

  private async handleCache(
    key: string,
    bypassCache: boolean,
  ): Promise<null | Record<string, any>> {
    if (bypassCache) return null
    const cached = await this.cacheService.get(key)
    return cached && cached !== {} ? cached : null
  }

  public async userGetInfo(
    user: string,
    bypassCache = false,
  ): Promise<LastfmUserInfo | null> {
    const key = `cache:lastfm:user:${user}`
    const cached = await this.handleCache(key, bypassCache)
    if (cached) return cached as LastfmUserInfo

    try {
      const { user: lastfmUser } = await this.request('user.getInfo', { user })

      const usr = {
        playCount: parseInt(lastfmUser.playcount),
        registered: lastfmUser.registered.unixtime,
        user: lastfmUser.name,
        name: lastfmUser.realname,
        image: lastfmUser.image[3]['#text'],
      }

      this.cacheService.set(key, usr, {
        ttl: 60 * 60,
      })

      return usr
    } catch (error) {
      if (
        error instanceof LastfmException &&
        error.message === 'User not found'
      )
        return null
      else throw error
    }
  }

  public async getAlbumsChart(
    user: string,
    period: PeriodResolvable,
    limit?: number,
  ) {
    if (Array.isArray(period)) {
      // Custom period
      period
    } else {
      const { topalbums } = await this.request('user.getTopAlbums', {
        user,
        period: period.toLowerCase(),
        limit,
      })

      return {
        fromWeekly: true,
        items: topalbums.album.slice(0, limit).map((album) => ({
          name: album.name,
          artist: album.artist.name,
          playCount: parseInt(album.playcount),
          image: LastfmService.parseImage(album.image, defaultAlbumImage),
        })),
      }
    }
  }

  static parseImage(images: LastfmImages, defaultImage: string) {
    return images[3]['#text'] && images[3]['#text'] !== ''
      ? images[3]['#text']
      : defaultImage
  }
}
