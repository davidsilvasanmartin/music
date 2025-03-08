import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { ErrorHandler, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { authCookieInterceptor } from './auth/interceptors/auth-cookie-interceptor';
import { AuthEffects } from './auth/store/effects';
import { authReducer } from './auth/store/reducer';
import { ErrorHandlerService } from './error-handler.service';
import { PlayerModule } from './player';
import { UiModule } from './ui/ui.module';

/**
 * TODO 1: the auth flow works but does not persist when the browser page is refreshed.
 *  This is because the User is not saved in the store. we need to first send a request
 *  to /user to see whether the User can be fetched (which means we are logged in because
 *  we are sending the right cookie), or whether we get a 401. The cookie is working fine.
 * TODO 2: There are some errors logged by Spring when the front-end is fetching images.
 */
@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    PlayerModule,
    UiModule,
    StoreModule.forRoot({ auth: authReducer }),
    EffectsModule.forRoot([AuthEffects]),
    // TODO this should not be here in production
    StoreDevtoolsModule.instrument(),
  ],
  providers: [
    { provide: ErrorHandler, useClass: ErrorHandlerService },
    provideHttpClient(withInterceptors([authCookieInterceptor])),
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
