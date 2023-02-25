import { log } from '@musicorum/coloris'
import { getWorkerForTheme, workers } from '@services/workers/workersService.js'
import { HttpError } from '@utils/HttpError.js'
import { nanoid } from 'nanoid'
import { themes } from '~collages/themes/themes.js'
import { CollagePayload } from '~collages/types/themes.js'

export async function generateCollage(payload: CollagePayload, appId: string) {
  const start = performance.now()
  // const worker = getWorkerForTheme(payload.theme) @todo
  const worker = workers[0]
  log.info('collages.generate', 'generating collage for theme ' + payload.theme)

  if (!worker) {
    log.warn(
      'collages.generate',
      'no available worker for theme ' + payload.theme
    )
    throw new HttpError('No worker available for this theme', 503)
  }

  const theme = themes[payload.theme]

  const options = theme.validationSchema.validateSync(payload.options)

  const workerPayload = await theme.handlePayload({
    ...payload,
    options
  })

  const id = nanoid(24)

  console.log(workerPayload)

  const result = await worker.generate({
    id,
    theme: 'classic_collage',
    hide_username: payload.hide_username,
    user: null,
    story: false,
    data: workerPayload
  })

  const end = performance.now()

  return {
    result,
    workerPayload,
    duration: end - start
  }
}
