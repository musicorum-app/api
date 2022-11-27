export enum Period {
  SEVEN_DAYS = '7DAY',
  ONE_MONTH = '1MONTH',
  THREE_MONTHS = '3MONTH',
  SIX_MONTHS = '6MONTH',
  TWELVE_MONTHS = '12MONTH',
  OVERALL = 'OVERALL'
}

export type PeriodResolvable = Period | [number, number]

export enum Entity {
  ARTIST = 'ARTIST',
  ALBUM = 'ALBUM',
  TRACK = 'TRACK'
}

export const defaultArtistImage =
  'https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.jpg'
export const defaultTrackImage =
  'https://lastfm.freetls.fastly.net/i/u/300x300/4128a6eb29f94943c9d206c08e625904.jpg'
export const defaultAlbumImage =
  'https://lastfm.freetls.fastly.net/i/u/300x300/c6f59c1e5e7240a4c0d427abd71f3dbb.jpg'
export const defaultUserImage =
  'https://lastfm.freetls.fastly.net/i/u/300x300/818148bf682d429dc215c1705eb27b98.jpg'
