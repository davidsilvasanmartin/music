import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';

import { AlbumComponent } from '../shared/album/album.component';
import { SearchModule } from '../ui/search';
import { SortModule } from '../ui/sort';
import { UiModule } from '../ui/ui.module';
import { ArtistsListComponent } from './artists-list/artists-list.component';
import { ArtistsEffects } from './artists-list/store/effects';
import { ArtistsRoutingModule } from './artists-routing.module';
import { ArtistsViewComponent } from './artists-view/artists-view.component';
import { artistsReducer } from './store/reducers';

@NgModule({
  declarations: [ArtistsListComponent, ArtistsViewComponent],
  imports: [
    CommonModule,
    ArtistsRoutingModule,
    UiModule,
    StoreModule.forFeature('artists', artistsReducer),
    EffectsModule.forFeature([ArtistsEffects]),
    SortModule,
    SearchModule,
    AlbumComponent,
  ],
})
export class ArtistsModule {}
