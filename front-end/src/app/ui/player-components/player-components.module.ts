import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { IconsModule } from '../icons/icons.module';
import { PlayerPlaylistAddComponent } from './player-playlist-add.component';
import { PlayerPlaylistRemoveComponent } from './player-playlist-remove.component';
import { PlayerPlaylistReplaceComponent } from './player-playlist-replace.component';

@NgModule({
  declarations: [
    PlayerPlaylistAddComponent,
    PlayerPlaylistRemoveComponent,
    PlayerPlaylistReplaceComponent,
  ],
  imports: [CommonModule, IconsModule],
  exports: [
    PlayerPlaylistAddComponent,
    PlayerPlaylistRemoveComponent,
    PlayerPlaylistReplaceComponent,
  ],
})
export class PlayerComponentsModule {}
