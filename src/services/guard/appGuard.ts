import { prisma } from '@services/database.js'
import { HttpError } from '@utils/HttpError.js'
import { FastifyReply, FastifyRequest } from 'fastify'

export async function requireApplicationGuard(
  request: FastifyRequest,
  reply: FastifyReply
) {
  const authroization = request.headers.authorization?.split(' ')

  const authType = authroization?.[0]

  let key: string | undefined

  if (authType?.toLowerCase() === 'app') {
    key = authroization?.[1]
  } else {
    key = (request.query as Record<string, string | undefined>).api_key
  }

  if (!key) {
    throw new HttpError('Invalid authentication method', 401)
  }

  const app = await prisma.application.findUnique({
    where: {
      key
    }
  })

  if (!app) {
    throw new HttpError('Invalid application key', 401)
  }

  return app
}
