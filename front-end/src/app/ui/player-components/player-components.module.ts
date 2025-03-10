import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { IconsModule } from '../icons/icons.module';
import { PlayerPlaylistAddComponent } from './player-playlist-add.component';
import { PlayerPlaylistRemoveComponent } from './player-playlist-remove.component';
import { PlayerPlaylistRemoveNoBgComponent } from './player-playlist-remove-no-bg.component';
import { PlayerPlaylistReplaceComponent } from './player-playlist-replace.component';
import { PlayerUserPlaylistAddNoBgComponent } from './player-user-playlist-add-no-bg.component';

// TODO probably move this module into player module
@NgModule({
  declarations: [
    PlayerPlaylistAddComponent,
    PlayerPlaylistRemoveComponent,
    PlayerPlaylistReplaceComponent,
    PlayerPlaylistRemoveNoBgComponent,
    PlayerUserPlaylistAddNoBgComponent,
  ],
  imports: [CommonModule, IconsModule],
  exports: [
    PlayerPlaylistAddComponent,
    PlayerPlaylistRemoveComponent,
    PlayerPlaylistReplaceComponent,
    PlayerPlaylistRemoveNoBgComponent,
    PlayerUserPlaylistAddNoBgComponent,
  ],
})
export class PlayerComponentsModule {}
