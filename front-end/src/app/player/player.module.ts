import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { StoreModule } from '@ngrx/store';

import { UiModule } from '../ui/ui.module';
import { PlayerPlaylistAddComponent } from './controls/player-playlist-add.component';
import { PlayerPlaylistRemoveComponent } from './controls/player-playlist-remove.component';
import { PlayerPlaylistReplaceComponent } from './controls/player-playlist-replace.component';
import { PlayerComponent } from './player.component';
import { PlayerPlaylistComponent } from './player-playlist/player-playlist.component';
import { playlistReducer } from './store/reducers';

@NgModule({
  declarations: [
    PlayerComponent,
    PlayerPlaylistRemoveComponent,
    PlayerPlaylistAddComponent,
    PlayerPlaylistReplaceComponent,
    PlayerPlaylistComponent,
  ],
  imports: [
    CommonModule,
    UiModule,
    StoreModule.forFeature('playlist', playlistReducer),
  ],
  exports: [
    PlayerPlaylistRemoveComponent,
    PlayerComponent,
    PlayerPlaylistRemoveComponent,
    PlayerPlaylistAddComponent,
    PlayerPlaylistReplaceComponent,
  ],
})
export class PlayerModule {}
