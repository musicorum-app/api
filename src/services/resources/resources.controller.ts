import { Body, Controller, Param, Post, Query, UseGuards } from '@nestjs/common'
import { ApiKeyGuard } from 'src/guards/api-key.guard'
import {
  albumResourcesPayloadJoi,
  artistResourcesPayloadJoi,
  trackResourcesPayloadJoi
} from '../validation/resources.joi'
import { ValidationException } from 'src/exceptions/validation.exception'
import { ResourcesService } from './resources.service'
import { AlbumRequestItem, TrackRequestItem } from './resources.type'

@Controller({
  path: 'resources',
  version: '2'
})
export class ResourcesController {
  constructor(private resourcesProvider: ResourcesService) {}

  @Post('artists')
  @UseGuards(ApiKeyGuard)
  findArtists(@Body() body: { artists: string[] }, @Query() query) {
    const validation = artistResourcesPayloadJoi.validate(body)
    if (validation.error) {
      throw new ValidationException(validation.error)
    }
    return this.resourcesProvider.findArtists(body.artists, query)
  }

  @Post('albums')
  @UseGuards(ApiKeyGuard)
  findAlbums(@Body() body: { albums: AlbumRequestItem[] }, @Query() query) {
    const validation = albumResourcesPayloadJoi.validate(body)
    if (validation.error) {
      throw new ValidationException(validation.error)
    }
    return this.resourcesProvider.findAlbums(body.albums, query)
  }

  @Post('tracks')
  @UseGuards(ApiKeyGuard)
  findTracks(@Body() body: { tracks: TrackRequestItem[] }, @Query() query) {
    const validation = trackResourcesPayloadJoi.validate(body)
    if (validation.error) {
      throw new ValidationException(validation.error)
    }
    return this.resourcesProvider.findTracks(body.tracks, query)
  }
}
