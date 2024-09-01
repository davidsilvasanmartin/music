import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { StoreModule } from '@ngrx/store';

import { UiModule } from '../ui/ui.module';
import { PlaylistPlayAlbumComponent } from './album/controls/playlist-play/playlist-play-album.component';
import { PlaylistReplaceComponent } from './album/controls/playlist-replace/playlist-replace.component';
import { PlayerComponent } from './player.component';
import { PlaylistComponent } from './playlist/playlist.component';
import { PlaylistPlayComponent } from './song/controls/playlist-play/playlist-play.component';
import { PlaylistRemoveComponent } from './song/controls/playlist-remove/playlist-remove.component';
import { SongComponent } from './song/song.component';
import { playlistReducer } from './store/reducers';

@NgModule({
  declarations: [
    PlayerComponent,
    PlaylistRemoveComponent,
    PlaylistPlayComponent,
    SongComponent,
    PlaylistPlayAlbumComponent,
    PlaylistReplaceComponent,
    PlaylistComponent,
  ],
  imports: [
    CommonModule,
    UiModule,
    StoreModule.forFeature('playlist', playlistReducer),
  ],
  exports: [
    PlaylistRemoveComponent,
    PlayerComponent,
    SongComponent,
    PlaylistPlayComponent,
    PlaylistRemoveComponent,
    PlaylistPlayAlbumComponent,
    PlaylistReplaceComponent,
  ],
})
export class PlayerModule {}
