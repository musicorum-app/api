import Fastify from 'fastify'
import cors from '@fastify/cors'
import { authRoutes } from '~auth/routes/index'
import { collagesRoutes } from '~collages/routes/index'
import { HttpError } from '@utils/HttpError'
import { ValidationError } from 'yup'
import { log } from '@musicorum/coloris'

const server = Fastify()

server.register(cors)

// routes
server.register(authRoutes, { prefix: 'auth' })
server.register(collagesRoutes, { prefix: 'collages' })

server.setErrorHandler((error, request, reply) => {
  if (error instanceof HttpError) {
    return reply.status(error.statusCode || 500).send({
      statusCode: error.statusCode || 500,
      error: 'Something went wrong',
      message: error.message
    })
  } else if (error instanceof ValidationError) {
    return reply.status(400).send({
      statusCode: 400,
      error: 'Validation error',
      message: error.message,
      validation: error.errors
    })
  } else {
    throw error
  }
})

server.listen(
  {
    port: 3000
  },
  (error, address) => {
    if (error) {
      console.error(error)
      process.exit(1)
    }

    log.info('server', `listening at ${address}`)
  }
)
