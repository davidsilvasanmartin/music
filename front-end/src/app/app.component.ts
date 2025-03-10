import { Component, OnInit } from '@angular/core';

import { select, Store } from '@ngrx/store';

import * as authSelectors from './auth/store/selectors';
import { AuthRootState } from './auth/store/state';

// TODO review styles and so on
@Component({
  selector: 'app-root',
  template: `<div
    class="flex min-h-screen w-full flex-col flex-nowrap bg-gray-200"
  >
    @if (isAuthenticated$ | async) {
      <div class="sticky top-0 z-10 w-full">
        <app-topbar></app-topbar>
      </div>
      <div class="flex w-full flex-grow flex-row flex-nowrap">
        <div class="w-64 min-w-64 border-r border-gray-300 bg-white shadow-sm">
          <app-sidebar></app-sidebar>
        </div>
        <div
          class="flex min-h-0 w-full flex-shrink flex-grow flex-col flex-nowrap overflow-y-auto p-4"
          style="max-width: 1500px"
        >
          <router-outlet></router-outlet>
        </div>
      </div>
      <div class="sticky bottom-0 z-10 self-stretch">
        <app-player />
      </div>
    } @else {
      <router-outlet></router-outlet>
    }
  </div>`,
})
export class AppComponent implements OnInit {
  isAuthenticated$ = this._store.pipe(select(authSelectors.isAuthenticated));

  constructor(private readonly _store: Store<AuthRootState>) {}

  ngOnInit(): void {
    // Check authentication status when app starts
    // this.authService.getCurrentUser().subscribe({
    //   error: () => {
    //     // Error handling is done in the service
    //     console.log('Initial auth check completed');
    //   },
    // });
  }
}
