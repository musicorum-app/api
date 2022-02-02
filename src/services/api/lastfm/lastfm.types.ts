export interface LastfmUserInfo {
  playCount: number
  name?: string
  user: string
  image?: string
  registered: string
}

export type LastfmImages = {
  size: string
  '#text': string
}[]

export interface LastfmTopAlbumsResponse {
  topalbums: {
    album: {
      name: string
      playcount: string
      image: LastfmImages
      artist: {
        name: string
      }
    }[]
  }
}
