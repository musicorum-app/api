import { HttpException, HttpStatus } from '@nestjs/common'
import { ValidationError } from 'joi'

export class ValidationException extends HttpException {
  constructor(validationError: ValidationError) {
    super(
      {
        error: 'Request invalid',
        message:
          'Something on the body is invalid. Check the validation_details field',
        validation_details: validationError.details
      },
      HttpStatus.BAD_REQUEST
    )
  }
}
