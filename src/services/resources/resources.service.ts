import { Injectable, Logger } from '@nestjs/common'
import { ConfigService } from '@nestjs/config'
import axios, { Axios } from 'axios'
import {
  AlbumRequestItem,
  AlbumResource,
  ArtistResource,
  IHaveImageResources,
  Nullable,
  ResourceFinderParams,
  TrackRequestItem,
  TrackResource
} from './resources.type'

@Injectable()
export class ResourcesService {
  private logger = new Logger(ResourcesService.name)
  private instance: Axios

  constructor(private configService: ConfigService) {
    if (!process.env.RESOURCES_URL) {
      this.logger.error(`'RESOURCES_URL' environment variable not present.`)
    }
    this.instance = axios.create({
      baseURL: process.env.RESOURCES_URL
    })
  }

  public async findArtists(artists: string[], params?: ResourceFinderParams) {
    this.logger.verbose(`Finding artists of length ${artists.length}`)
    return this.instance
      .post<Nullable<ArtistResource>[]>(
        '/find/artists',
        {
          artists
        },
        {
          params
        }
      )
      .then((res) => res.data)
  }

  public async findAlbums(
    albums: AlbumRequestItem[],
    params: ResourceFinderParams
  ) {
    this.logger.verbose(`Finding albums of length ${albums.length}`)
    return this.instance
      .post<AlbumResource[]>(
        '/find/albums',
        {
          albums
        },
        {
          params
        }
      )
      .then((res) => res.data)
  }

  public async findTracks(
    tracks: TrackRequestItem[],
    params?: ResourceFinderParams
  ) {
    this.logger.verbose(`Finding tracks of length ${tracks.length}`)
    return this.instance
      .post<TrackResource[]>(
        '/find/tracks',
        {
          tracks
        },
        {
          params
        }
      )
      .then((res) => res.data)
  }

  static getPrefferedImageResource(resource: IHaveImageResources) {
    return resource.resources.find(
      (r) => r.hash === resource.preferred_resource
    )
  }
}

export const getPrefferedImageResource =
  ResourcesService.getPrefferedImageResource
