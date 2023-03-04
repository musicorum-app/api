import { requireApplicationGuard } from '@services/guard/appGuard.js'
import { FastifyReply, FastifyRequest } from 'fastify'
import { generateCollage } from '~collages/modules/collage.js'
import { themes } from '~collages/themes/themes'
import { collagePayloadValidation } from '~collages/validation/collage.js'

export async function generateController(
  req: FastifyRequest,
  reply: FastifyReply
) {
  const app = await requireApplicationGuard(req, reply)

  const generationPayload = collagePayloadValidation.validateSync(req.body)

  const options = themes[generationPayload.theme].validationSchema.validateSync(
    generationPayload.options
  )

  const payload = {
    ...generationPayload,
    options
  }

  return generateCollage(payload, app.id)
}
