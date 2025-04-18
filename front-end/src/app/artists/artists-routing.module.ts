import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ArtistsListComponent } from './artists-list/artists-list.component';
import { ArtistsViewComponent } from './artists-view/artists-view.component';

const routes: Routes = [
  { path: '', component: ArtistsListComponent, pathMatch: 'full' },
  { path: ':artistId', component: ArtistsViewComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ArtistsRoutingModule {}
