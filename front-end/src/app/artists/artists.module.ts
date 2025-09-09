import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';

import { AlbumComponent } from '../shared/album-components/album.component';
import { AlbumCountComponent } from '../shared/album-components/album-count.component';
import { MbLinkComponent } from '../shared/mb-link/mb-link.component';
import { CardModule } from '../ui/card/card.module';
import { PaginationModule } from '../ui/pagination';
import { SearchModule } from '../ui/search';
import { SortModule } from '../ui/sort';
import { ArtistsListComponent } from './artists-list/artists-list.component';
import { ArtistsListEffects } from './artists-list/store/effects';
import { ArtistsRoutingModule } from './artists-routing.module';
import { ArtistsViewComponent } from './artists-view/artists-view.component';
import { ArtistsViewEffects } from './artists-view/store/effects';
import { artistsReducer } from './store/reducers';

@NgModule({
  declarations: [ArtistsListComponent, ArtistsViewComponent],
  imports: [
    CommonModule,
    ArtistsRoutingModule,
    StoreModule.forFeature('artists', artistsReducer),
    EffectsModule.forFeature([ArtistsListEffects, ArtistsViewEffects]),
    SortModule,
    SearchModule,
    AlbumComponent,
    AlbumCountComponent,
    MbLinkComponent,
    CardModule,
    PaginationModule,
  ],
})
export class ArtistsModule {}
