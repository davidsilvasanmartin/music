import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';

import { AlbumComponent } from '../shared/album/album.component';
import { SearchModule } from '../ui/search';
import { SortModule } from '../ui/sort';
import { UiModule } from '../ui/ui.module';
import { ArtistsComponent } from './artists.component';
import { ArtistsRoutingModule } from './artists-routing.module';
import { ArtistsEffects } from './store/effects';
import { artistsReducer } from './store/reducers';

@NgModule({
  declarations: [ArtistsComponent],
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
