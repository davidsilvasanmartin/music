import { Component, OnInit } from '@angular/core';

import { select, Store } from '@ngrx/store';

import * as authSelectors from './auth/store/selectors';
import { AuthRootState } from './auth/store/state';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
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
