import { Logger, VersioningType } from '@nestjs/common'
import { NestFactory } from '@nestjs/core'
import { MusicorumModule } from './app.module'

async function main() {
  const logger = new Logger('MusicorumApi')
  const app = await NestFactory.create(MusicorumModule)

  app.enableVersioning({
    type: VersioningType.URI
  })

  await app.listen(3000)
  logger.log(`Server listening on :3000`)
}

main()
