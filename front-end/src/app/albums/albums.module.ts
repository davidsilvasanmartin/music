import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';

import { AlbumsComponent } from './albums.component';
import { UiModule } from '../ui/ui.module';
import { PlaylistModule } from '../playlist';
import { albumsReducer } from './store/reducers';
import { AlbumsEffects } from './store/effects';
import { AlbumsSongComponent } from './albums-song/albums-song.component';

@NgModule({
  declarations: [AlbumsComponent, AlbumsSongComponent],
  imports: [
    CommonModule,
    UiModule,
    PlaylistModule,
    StoreModule.forFeature('albums', albumsReducer),
    EffectsModule.forFeature([AlbumsEffects]),
  ],
  exports: [AlbumsComponent],
})
export class AlbumsModule {}
