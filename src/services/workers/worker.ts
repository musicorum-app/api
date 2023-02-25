import { ThemeName } from '~collages/types/themes.js'

export interface WorkerCollageResponse {
  file: string
  duration: number
}

export interface WorkerCollagePayload {
  id: string
  user: null
  theme: string | ThemeName
  story: boolean
  hide_username: boolean
  data: unknown
}

export class Worker {
  public name: string
  public engine: string
  public version: string
  public scheme: string
  public themes: string[]
  public available = false
  public url: string

  constructor(url: string) {
    this.url = url
  }

  async setup() {
    const metadata = await fetch(`${this.url}/metadata`).then((r) => r.json())

    if (
      !metadata.name ||
      !metadata.engine ||
      !metadata.version ||
      !metadata.scheme ||
      !metadata.themes
    ) {
      throw new Error('Could not validate worker')
    }

    this.name = metadata.name
    this.engine = metadata.engine
    this.version = metadata.version
    this.scheme = metadata.scheme
    this.themes = metadata.themes
    this.available = true
  }

  async generate(payload: WorkerCollagePayload) {
    return fetch(`${this.url}/generate`, {
      method: 'post',
      headers: {
        'content-type': 'application/json'
      },
      body: JSON.stringify(payload)
    }).then((r) => r.json() as unknown as WorkerCollageResponse)
  }
}
