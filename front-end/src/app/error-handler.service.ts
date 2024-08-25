import { ErrorHandler, Injectable } from '@angular/core';

@Injectable()
export class ErrorHandlerService extends ErrorHandler {
  override handleError(error: unknown) {
    console.error(error);
    super.handleError(error);
  }
}
