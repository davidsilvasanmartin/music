import { Component, OnInit } from '@angular/core';

import { select, Store } from '@ngrx/store';

import * as authSelectors from './auth/store/selectors';
import { AuthRootState } from './auth/store/state';

@Component({
  selector: 'app-root',
  template: `<div
    class="flex min-h-screen w-full flex-col flex-nowrap items-center bg-gray-200"
  >
    @if (isAuthenticated$ | async) {
      <app-topbar></app-topbar>
      <div
        class="flex min-h-0 w-full flex-shrink flex-grow flex-row flex-nowrap overflow-y-auto p-4"
        style="max-width: 1500px"
      >
        <router-outlet></router-outlet>
      </div>
      <div class="sticky bottom-0 self-stretch">
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
