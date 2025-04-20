import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PlaylistsListComponent } from './playlists-list/playlists-list.component';
import { PlaylistsViewComponent } from './playlists-view/playlists-view.component';

const routes: Routes = [
  { path: '', component: PlaylistsListComponent, pathMatch: 'full' },
  { path: ':playlistId', component: PlaylistsViewComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PlaylistsRoutingModule {}
