import { getPrefferedImageResource } from './resources.service'
import { ImageResourceSource, ImageSize } from './resources.type'

describe('ResourcesService', () => {
  it('should resolve the preffered resource', () => {
    const resource = {
      hash: '5c5f9f8668259dd7d5be8b2466c51c48de9ee5c5',
      name: 'Planet Her',
      spotify_id: '1nAQbHeOWTfQzbOoFrvndW',
      deezer_id: null,
      artists: ['Doja Cat'],
      tags: ['rnb', 'trap', 'pop rap', 'rap', 'afrobeat'],
      release_date: '2021-06-25',
      preferred_resource: '76a1647614e8431536bbb0c09b1ec2a076d5f9c4',
      resources: [
        {
          hash: '4a273c1a45f8d49e10d1247a99beb620ee3cd4dc',
          explicit: null,
          source: 'SPOTIFY' as ImageResourceSource,
          color_palette: {
            vibrant: '#2661b8',
            dark_vibrant: '#093c81',
            light_vibrant: '#e7e5c9',
            muted: '#b36689',
            dark_muted: '#335d5e',
            light_muted: '#69662f'
          },
          active: true,
          created_at: '1656196921302',
          images: [
            {
              hash: 'be3dd5699f51b50f960a3c41a4474223e5fa5668',
              url: 'https://i.scdn.co/image/ab67616d0000b2734df3245f26298a1579ecc321',
              size: 'LARGE' as ImageSize
            }
          ]
        },
        {
          hash: '76a1647614e8431536bbb0c09b1ec2a076d5f9c4',
          explicit: null,
          source: 'LASTFM' as ImageResourceSource,
          color_palette: {
            vibrant: '#1060b9',
            dark_vibrant: '#153d84',
            light_vibrant: '#84b9f4',
            muted: '#7764b3',
            dark_muted: '#68314c',
            light_muted: '#be7c94'
          },
          active: true,
          created_at: '1657388902343',
          images: [
            {
              hash: '691f7ae7dfc3b61a53b63b1187964a3c01243d42',
              url: 'https://lastfm.freetls.fastly.net/i/u/300x300/d1619e7707eb9f63884cebce1f76b382.jpg',
              size: 'MEDIUM' as ImageSize
            }
          ]
        }
      ],
      created_at: '1656207843688',
      updated_at: '1657388902430'
    }

    const preferred = getPrefferedImageResource(resource)

    expect(preferred?.hash).toBe(resource.preferred_resource)
  })
})
