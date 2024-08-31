import { HttpClientModule } from '@angular/common/http';
import { ErrorHandler, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';

import { AlbumsModule } from './albums/albums.module';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { ErrorHandlerService } from './error-handler.service';
import { PlaylistModule } from './playlist';
import { reducers } from './store/reducers';
import { UiModule } from './ui/ui.module';

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
