import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StoreModule } from '@ngrx/store';

import { UiModule } from '../ui/ui.module';
import { playlistReducer } from './store/reducers';
import { PlaylistComponent } from './playlist.component';
import { PlaylistRemoveComponent } from './song/controls/playlist-remove/playlist-remove.component';
import { PlaylistPlayComponent } from './song/controls/playlist-play/playlist-play.component';
import { SongComponent } from './song/song.component';
import { AlbumComponent } from './album/album.component';
import { PlaylistPlayAlbumComponent } from './album/controls/playlist-play/playlist-play-album.component';

@NgModule({
  declarations: [
    PlaylistComponent,
    PlaylistRemoveComponent,
    PlaylistPlayComponent,
    SongComponent,
    AlbumComponent,
    PlaylistPlayAlbumComponent,
  ],
  imports: [
    CommonModule,
    UiModule,
    StoreModule.forFeature('playlist', playlistReducer),
  ],
  exports: [
    PlaylistRemoveComponent,
    PlaylistComponent,
    SongComponent,
    PlaylistPlayComponent,
    PlaylistRemoveComponent,
    AlbumComponent,
    PlaylistPlayAlbumComponent,
  ],
})
export class PlaylistModule {}
