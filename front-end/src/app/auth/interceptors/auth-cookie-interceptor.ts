import { HttpEvent, HttpHandlerFn, HttpRequest } from "@angular/common/http";

import { Observable } from "rxjs";

/**
 * This function is needed because otherwise the browser won't save or send back the session id cookie.
 * See https://stackoverflow.com/questions/35602866/how-to-send-cookie-in-request-header-for-all-the-requests-in-angular2/35603479#35603479
 */
export const authCookieInterceptor = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn,
): Observable<HttpEvent<unknown>> => {
  const reqWithAuthCookie = req.clone({ withCredentials: true });
  return next(reqWithAuthCookie);
};
