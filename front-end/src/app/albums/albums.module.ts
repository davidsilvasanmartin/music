import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';

import { AlbumComponent } from '../shared/album/album.component';
import { CardModule } from '../ui/card/card.module';
import { PaginationModule } from '../ui/pagination';
import { SearchModule } from '../ui/search';
import { SortModule } from '../ui/sort';
import { AlbumsComponent } from './albums.component';
import { AlbumsRoutingModule } from './albums-routing.module';
import { AlbumsEffects } from './store/effects';
import { albumsReducer } from './store/reducers';

@NgModule({
  declarations: [AlbumsComponent],
  imports: [
    CommonModule,
    AlbumsRoutingModule,
    StoreModule.forFeature('albums', albumsReducer),
    EffectsModule.forFeature([AlbumsEffects]),
    SortModule,
    SearchModule,
    AlbumComponent,
    CardModule,
    PaginationModule,
  ],
})
export class AlbumsModule {}
