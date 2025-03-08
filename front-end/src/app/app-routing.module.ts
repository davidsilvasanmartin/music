import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './auth/components/login/login.component';
import { authenticationGuard } from './auth/guards/authentication-guard';
import { guestGuard } from './auth/guards/guest-guard';

const routes: Routes = [
  { path: '', redirectTo: '/albums', pathMatch: 'full' },
  {
    path: 'auth/login',
    component: LoginComponent,
    canActivate: [guestGuard],
  },
  {
    path: 'albums',
    loadChildren: () =>
      import('./albums/albums.module').then((m) => m.AlbumsModule),
    canActivate: [authenticationGuard],
  },
  // { path: '**', redirectTo: '/' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
