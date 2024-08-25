import { ErrorHandler, NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AlbumsModule } from './albums/albums.module';
import { UiModule } from './ui/ui.module';
import { reducers } from './store/reducers';
import { PlaylistModule } from './playlist';
import { ErrorHandlerService } from './error-handler.service';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    UiModule,
    AlbumsModule,
    PlaylistModule,
    StoreModule.forRoot(reducers),
    EffectsModule.forRoot([]),
    // TODO this should not be here in production
    StoreDevtoolsModule.instrument(),
  ],
  providers: [{ provide: ErrorHandler, useClass: ErrorHandlerService }],
  bootstrap: [AppComponent],
})
export class AppModule {}
