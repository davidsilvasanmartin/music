import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';

import { AlbumComponent } from '../shared/album-components/album.component';
import { AlbumCountComponent } from '../shared/album-components/album-count.component';
import { MbLinkComponent } from '../shared/mb-link/mb-link.component';
import { QueueComponentsModule } from '../shared/queue-components/queue-components.module';
import { CardModule } from '../ui/card/card.module';
import { IconsModule } from '../ui/icons/icons.module';
import { SearchModule } from '../ui/search';
import { SortModule } from '../ui/sort';
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
    StoreModule.forFeature('playlists', playlistsReducer),
    EffectsModule.forFeature([PlaylistsListEffects, PlaylistsViewEffects]),
    SortModule,
    SearchModule,
    AlbumComponent,
    AlbumCountComponent,
    MbLinkComponent,
    IconsModule,
    QueueComponentsModule,
    CardModule,
  ],
})
export class PlaylistsModule {}
