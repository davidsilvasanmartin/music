import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { StoreModule } from '@ngrx/store';

import { UiModule } from '../ui/ui.module';
import { PlayerComponent } from './player.component';
import { PlayerPlaylistComponent } from './player-playlist/player-playlist.component';
import { playlistReducer } from './store/reducers';

@NgModule({
  declarations: [PlayerComponent, PlayerPlaylistComponent],
  imports: [
    CommonModule,
    UiModule,
    StoreModule.forFeature('playlist', playlistReducer),
  ],
  exports: [PlayerComponent],
})
export class PlayerModule {}
