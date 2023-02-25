import { log } from '@musicorum/coloris'
import { ThemeName } from '~collages/types/themes.js'
import { Worker } from './Worker.js'

export const workers = [] as Worker[]
const maxTries = 10

export function getWorkerForTheme(theme: ThemeName) {
  return workers.find((w) => w.themes.includes(theme))
}

async function setupWorkers() {
  log.info('workers.init', 'initializing workers')
  const urls = process.env.WORKERS?.split(';')

  if (!urls) {
    return log.warn('workers.init', 'no available workers to initialize')
  }

  await Promise.all(
    urls.map(async (url) => {
      if (url.endsWith('/')) {
        url = url.slice(0, -1)
      }

      const worker = new Worker(url)
      workers.push(worker)
      await setupWorker(worker)
    })
  )
}

setupWorkers()

async function setupWorker(worker: Worker, tries = 0) {
  try {
    await worker.setup()

    log.info(
      'workers.init',
      `worker '${worker.name}' running at ${worker.url} using ${worker.engine}@${worker.version} initialized`
    )
  } catch (error) {
    log.warn(
      'workers.init',
      `Could not connect to worker at ${worker.url}: '${error}'. Retrying in 5s (${tries}/${maxTries})...`
    )

    if (tries < maxTries) {
      setTimeout(() => {
        setupWorker(worker, ++tries)
      }, 5000)
    }
  }
}
