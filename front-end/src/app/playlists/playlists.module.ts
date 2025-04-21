import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';

import { AlbumComponent } from '../shared/album/album.component';
import { AlbumCountComponent } from '../shared/album-count/album-count.component';
import { MbLinkComponent } from '../shared/mb-link/mb-link.component';
import { SearchModule } from '../ui/search';
import { SortModule } from '../ui/sort';
import { UiModule } from '../ui/ui.module';
import { ExtractSongsPipe } from './extract-songs.pipe';
import { PlaylistsListComponent } from './playlists-list/playlists-list.component';
import { PlaylistsListEffects } from './playlists-list/store/effects';
import { PlaylistsRoutingModule } from './playlists-routing.module';
import { PlaylistsViewComponent } from './playlists-view/playlists-view.component';
import { PlaylistsViewEffects } from './playlists-view/store/effects';
import { playlistsReducer } from './store/reducers';

@NgModule({
  declarations: [
    PlaylistsListComponent,
    PlaylistsViewComponent,
    ExtractSongsPipe,
  ],
  imports: [
    CommonModule,
    PlaylistsRoutingModule,
    UiModule,
    StoreModule.forFeature('playlists', playlistsReducer),
    EffectsModule.forFeature([PlaylistsListEffects, PlaylistsViewEffects]),
    SortModule,
    SearchModule,
    AlbumComponent,
    AlbumCountComponent,
    MbLinkComponent,
  ],
})
export class PlaylistsModule {}
