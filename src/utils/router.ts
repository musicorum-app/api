import { FastifyInstance } from 'fastify'

type RouterScope = (fastify: FastifyInstance) => void

export function createRouter(routerFunction: RouterScope) {
  return (fastify: FastifyInstance, _: unknown, done: () => void) => {
    routerFunction(fastify)

    done()
  }
}
