import { CACHE_MANAGER, Inject, Injectable, Logger } from '@nestjs/common'
import axios from 'axios'
import { Cache } from 'cache-manager'
import { defaultAlbumImage, Period, PeriodResolvable } from 'src/constants'
import { LastfmException } from 'src/exceptions/lastfm.exception'
import { LastfmAlbumChart, LastfmImages, LastfmUserInfo } from './lastfm.types'
import LastClient from '@musicorum/lastfm'

const periods = {
  '7DAY': 604800000,
  '1MONTH': 2592000000,
  '3MONTH': 7776000000,
  '6MONTH': 15552000000,
  '12MONTH': 31536000000
} as const

@Injectable()
export class LastfmService {
  private logger = new Logger(LastfmService.name)
  private client: LastClient

  constructor(@Inject(CACHE_MANAGER) private cacheService: Cache) {
    if (process.env.LASTFM_KEY) {
      this.client = new LastClient(process.env.LASTFM_KEY)
    } else {
      this.logger.error(`'LASTFM_KEY' environment variable not present.`)
    }
  }

  public async request<D = Record<string, any>>(
    method: string,
    params: Record<string, any>
  ): Promise<D> {
    this.logger.verbose(`Doing request on last.fm for method '${method}'`)
    try {
      const res = await axios.get('http://ws.audioscrobbler.com/2.0/', {
        params: {
          method,
          format: 'json',
          api_key: process.env.LASTFM_KEY,
          ...params
        }
      })
      return res.data
    } catch (error) {
      if (
        error.isAxiosError &&
        error.response.data &&
        error.response.data.error
      ) {
        this.logger.warn(
          `Last.fm api error: ${error.response.data.message} (${error.response.data.error})`
        )
        throw new LastfmException(error.response.data)
      } else {
        throw error
      }
    }
  }

  private async handleCache(
    key: string,
    bypassCache: boolean
  ): Promise<null | Record<string, any>> {
    if (bypassCache) return null
    const cached = await this.cacheService.get(key)
    return cached && typeof cached === 'object' && Object.keys(cached).length
      ? cached
      : null
  }

  public async userGetInfo(
    user: string,
    bypassCache = false
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
        image: lastfmUser.image[3]['#text']
      }

      this.cacheService.set(key, usr, {
        ttl: 60 * 60
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
    limit?: number
  ) {
    if (Array.isArray(period)) {
      // Custom period
      return {
        fromWeekly: true,
        items: []
      }
    } else {
      const { topalbums } = await this.request('user.getTopAlbums', {
        user,
        period: period.toLowerCase(),
        limit
      })

      return {
        fromWeekly: false,
        items: topalbums.album.slice(0, limit).map((album) => ({
          name: album.name,
          artist: album.artist.name,
          playCount: parseInt(album.playcount),
          image: LastfmService.parseImage(album.image, defaultAlbumImage)
        })) as LastfmAlbumChart[]
      }
    }
  }

  public async getArtistsChart(
    user: string,
    period: PeriodResolvable,
    limit?: number
  ) {
    if (Array.isArray(period)) {
      // Custom period
      return {
        fromWeekly: true,
        items: []
      }
    } else {
      const { topartists } = await this.request('user.getTopArtists', {
        user,
        period: period.toLowerCase(),
        limit
      })

      return {
        fromWeekly: false,
        items: topartists.artist.slice(0, limit).map((artist) => ({
          name: artist.name,
          playCount: parseInt(artist.playcount)
        })) as { name: string; playCount: number }[]
      }
    }
  }

  public async getTracksChart(
    user: string,
    period: PeriodResolvable,
    limit?: number
  ) {
    if (Array.isArray(period)) {
      // Custom period
      return {
        fromWeekly: true,
        items: []
      }
    } else {
      const { toptracks } = await this.request('user.getTopTracks', {
        user,
        period: period.toLowerCase(),
        limit
      })

      return {
        fromWeekly: false,
        items: toptracks.track.slice(0, limit).map((track) => ({
          name: track.name,
          artist: track.artist.name,
          playCount: parseInt(track.playcount)
        })) as { name: string; artist: string; playCount: number }[]
      }
    }
  }

  public async getScrobbleCount(user: string, period: PeriodResolvable) {
    if (period === Period.OVERALL) {
      const playCount = await this.userGetInfo(user).then((r) => r?.playCount)
      return playCount || 0
    }

    let from: Date
    let to: Date

    if (Array.isArray(period)) {
      from = new Date(period[0])
      to = new Date(period[1])
    } else {
      const now = new Date()
      from = new Date(now.getTime() - periods[period])
      to = now
    }

    const recentTracks = await this.client.user.getRecentTracks(user, {
      limit: 1,
      from,
      to
    })
    return recentTracks.attr.total
  }

  static parseImage(images: LastfmImages, defaultImage: string) {
    return images[3]['#text'] && images[3]['#text'] !== ''
      ? images[3]['#text']
      : defaultImage
  }
}
