import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';

import { PlaylistModule } from '../playlist';
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
    PlaylistModule,
    StoreModule.forFeature('albums', albumsReducer),
    EffectsModule.forFeature([AlbumsEffects]),
  ],
  exports: [AlbumsComponent],
})
export class AlbumsModule {}
