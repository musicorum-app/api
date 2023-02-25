import { createRouter } from '@utils/router.js'
import { applicationRoutes } from './applications.js'

export const authRoutes = createRouter((fastify) => {
  fastify.register(applicationRoutes)
})
