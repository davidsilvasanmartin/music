import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StoreModule } from '@ngrx/store';

import { UiModule } from '../ui/ui.module';
import { playlistReducer } from './store/reducers';
import { AddToPlaylistButtonComponent } from './add-to-playlist-button/add-to-playlist-button.component';
import { PlaylistComponent } from './playlist.component';
import { PlaylistSongComponent } from './playlist-song/playlist-song.component';
import { RemoveFromPlaylistButtonComponent } from './remove-from-playlist-button/remove-from-playlist-button.component';

@NgModule({
  declarations: [
    AddToPlaylistButtonComponent,
    PlaylistComponent,
    PlaylistSongComponent,
    RemoveFromPlaylistButtonComponent,
  ],
  imports: [
    CommonModule,
    UiModule,
    StoreModule.forFeature('player', playlistReducer),
  ],
  exports: [
    AddToPlaylistButtonComponent,
    RemoveFromPlaylistButtonComponent,
    PlaylistComponent,
  ],
})
export class PlaylistModule {}
