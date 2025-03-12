import { Component, OnInit } from '@angular/core';

import { select, Store } from '@ngrx/store';

import * as authSelectors from './auth/store/selectors';
import { AuthRootState } from './auth/store/state';

// TODO review styles and so on
@Component({
  selector: 'app-root',
  template: `<div
    class="m-0 flex h-screen w-screen flex-col flex-nowrap overflow-hidden bg-gray-200"
  >
    @if (isAuthenticated$ | async) {
      <!-- TODO The footer is taking space from the sidebar right now. It should not be like that -->
      <app-topbar></app-topbar>
      <div class="flex flex-1 flex-row flex-nowrap overflow-hidden">
        <app-sidebar></app-sidebar>
        <div class="flex-1 overflow-y-auto p-2 lg:p-4">
          <router-outlet></router-outlet>
        </div>
      </div>
      <app-player />
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
