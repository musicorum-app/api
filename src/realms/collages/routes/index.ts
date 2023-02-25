import { createRouter } from '@utils/router.js'
import { generateRoute } from './generate.js'
import { workerRoute } from './workers.js'

export const collagesRoutes = createRouter((fastify) => {
  fastify.register(workerRoute)
  fastify.register(generateRoute)
})
