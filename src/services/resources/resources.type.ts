export type Nullable<T> = T | null

export interface ResourceFinderParams {
  popularity?: number
  palette?: boolean
  sources?: string[]
}

export enum ImageResourceSource {
  Spotify = 'SPOTIFY',
  Deezer = 'DEEZER',
  LastFM = 'LASTFM'
}

export enum ImageSize {
  ExtraSmall = 'EXTRA_SMALL',
  Small = 'SMALL',
  Medium = 'MEDIUM',
  Large = 'LARGE',
  ExtraLarge = 'EXTRA_LARGE',
  Unknown = 'UNKNOWN'
}

export type ImageResponse = {
  hash: string
  size: ImageSize
  url: string
}

// Resources response
export interface ImageResourceResponse {
  hash: string
  explicit: Nullable<boolean>
  active: boolean
  source: ImageResourceSource
  images: ImageResponse[]
  color_palette: {
    vibrant: Nullable<string>
    dark_vibrant: Nullable<string>
    light_vibrant: Nullable<string>
    muted: Nullable<string>
    dark_muted: Nullable<string>
    light_muted: Nullable<string>
  }
  created_at: string
}

export interface AlbumRequestItem {
  name: string
  artist: string
}

export interface AlbumResource {
  hash: string
  name: string
  artists: string[]
  tags: string[]
  release_date: Nullable<string>

  spotify_id: Nullable<string>
  deezer_id: Nullable<number>

  resources: ImageResourceResponse[]
  preferred_resource: Nullable<string>

  created_at: string
  updated_at: Nullable<string>
}

export interface ArtistResource {
  hash: string
  name: string

  spotify_id: Nullable<string>
  deezer_id: Nullable<number>

  genres: string[]
  tags: string[]
  similar: string[]
  popularity: Nullable<number>

  resources: ImageResourceResponse[]
  preferred_resource: Nullable<string>

  created_at: string
  updated_at: Nullable<string>
}

export interface TrackRequestItem {
  name: string
  artist: string
  album?: string
}

export interface TrackResourceFeatures {
  danceability: number
  energy: number
  loudness: number
  speechiness: number
  acousticness: number
  instrumentalness: number
  liveness: number
  valence: number
  tempo: number
}

export interface TrackResource {
  hash: string
  name: string
  artists: string[]
  album: Nullable<string>

  spotify_id: Nullable<string>
  deezer_id: Nullable<number>
  genius_id: Nullable<number>

  tags: string[]
  duration: Nullable<number>
  preview: Nullable<string>
  explicit: Nullable<boolean>

  resources: ImageResourceResponse[]
  preferred_resource: Nullable<string>

  created_at: string
  updated_at: Nullable<string>

  features: Nullable<TrackResourceFeatures>
}
