import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StoreModule } from '@ngrx/store';

import { UiModule } from '../ui/ui.module';
import { playerReducer } from './store/reducers';
import { AddToPlaylistButtonComponent } from './add-to-playlist-button/add-to-playlist-button.component';
import { PlayerComponent } from './player.component';
import { PlaylistSongComponent } from './playlist-song/playlist-song.component';

@NgModule({
  declarations: [
    AddToPlaylistButtonComponent,
    PlayerComponent,
    PlaylistSongComponent,
  ],
  imports: [
    CommonModule,
    UiModule,
    StoreModule.forFeature('player', playerReducer),
  ],
  exports: [AddToPlaylistButtonComponent, PlayerComponent],
})
export class PlayerModule {}
