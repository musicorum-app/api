import { requireApplicationGuard } from '@services/guard/appGuard.js'
import { FastifyReply, FastifyRequest } from 'fastify'
import { generateCollage } from '~collages/modules/collage.js'
import { collagePayloadValidation } from '~collages/validation/collage.js'

export async function generateController(
  req: FastifyRequest,
  reply: FastifyReply
) {
  const app = await requireApplicationGuard(req, reply)

  const generationPayload = collagePayloadValidation.validateSync(req.body)

  return generateCollage(generationPayload, app.id)
}
