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
import { QueueModule } from './queue';
import { SidebarComponent } from './ui/sidebar/sidebar.component';
import { TopbarComponent } from './ui/topbar/topbar.component';
import { UiModule } from './ui/ui.module';

/**
 * TODO: There are some errors logged by Spring when the front-end is fetching images.
 */
@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    QueueModule,
    UiModule,
    TopbarComponent,
    SidebarComponent,
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
