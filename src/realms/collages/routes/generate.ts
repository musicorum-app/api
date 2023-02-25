import { createRouter } from '@utils/router.js'
import { generateController } from '~collages/controllers/generate.js'

export const generateRoute = createRouter((fastify) => {
  fastify.post('/generate', generateController)
})
