import { createRouter } from '@utils/router.js'
import { listApplicationsController } from '~auth/controllers/application/list.js'

export const applicationRoutes = createRouter((fastify) => {
  fastify.get('/applications', listApplicationsController)
})
