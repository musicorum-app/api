import { log } from '@musicorum/coloris'
import { LastClient } from '@musicorum/lastfm'

const key = process.env.LASTFM_KEY

if (!key) {
  log.error('lastfm', "'LASTFM_KEY' environment not defined")
}

export const lastfmClient = new LastClient(key!)
