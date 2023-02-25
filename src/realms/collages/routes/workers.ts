import { createRouter } from '@utils/router.js'
import { listWorkersController } from '~collages/controllers/workers.js'

export const workerRoute = createRouter((fastify) => {
  fastify.get('/workers', listWorkersController)
})
