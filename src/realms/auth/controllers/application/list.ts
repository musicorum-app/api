import { prisma } from '@services/database.js'

export function listApplicationsController() {
  return prisma.application.findMany({
    select: {
      id: true,
      name: true,
      key: true,
      created_at: true,
      updated_at: true
    }
  })
}
