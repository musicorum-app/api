import { HttpException, HttpStatus } from '@nestjs/common'

export class LastfmException extends HttpException {
  public message: string
  public error: number

  constructor({ error, message }: { message: string; error: number }) {
    super(
      `There was a problem comunicating with Last.fm (error ${error})`,
      HttpStatus.SERVICE_UNAVAILABLE
    )
    this.message = message
    this.error = error
  }
}
