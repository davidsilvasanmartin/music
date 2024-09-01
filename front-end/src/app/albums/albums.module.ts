import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';

import { PlayerModule } from '../player';
import { UiModule } from '../ui/ui.module';
import { AlbumsComponent } from './albums.component';
import { AlbumsSongComponent } from './albums-song/albums-song.component';
import { AlbumsEffects } from './store/effects';
import { albumsReducer } from './store/reducers';

@NgModule({
  declarations: [AlbumsComponent, AlbumsSongComponent],
  imports: [
    CommonModule,
    UiModule,
    // TODO remove this because it's importing a store
    PlayerModule,
    StoreModule.forFeature('albums', albumsReducer),
    EffectsModule.forFeature([AlbumsEffects]),
  ],
  exports: [AlbumsComponent],
})
export class AlbumsModule {}
