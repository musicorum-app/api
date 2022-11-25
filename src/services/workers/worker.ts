import axios from 'axios'
import { WorkerGenerationResponse } from 'src/types/common.js'

export class Worker {
  public name: string
  public engine: string
  public version: string
  public scheme: string
  public themes: string[]
  public available = false

  constructor(public url: string) {}

  async setup() {
    const { data } = await axios.default.get(`${this.url}/metadata`)

    if (
      !data.name ||
      !data.engine ||
      !data.version ||
      !data.scheme ||
      !data.themes
    ) {
      throw new Error('Could not validate worker')
    }

    this.name = data.name
    this.engine = data.engine
    this.version = data.version
    this.scheme = data.scheme
    this.themes = data.themes
    this.available = true
  }

  async generate(data: Record<string, any>, traceId?: string) {
    return axios.default
      .post<WorkerGenerationResponse>(`${this.url}/generate`, data, {
        headers: {
          'sentry-trace': traceId || ''
        }
      })
      .then((r) => r.data)
  }
}
