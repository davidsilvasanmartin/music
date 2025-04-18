import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  signal,
} from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Store } from '@ngrx/store';

import type { User } from '../../models';
import { AuthService } from '../../services/auth.service';
import * as authActions from '../../store/actions';
import type { AuthRootState } from '../../store/state';

@Component({
  selector: 'app-login',
  templateUrl: 'login.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  isAuthCheckInProgress = signal(true);

  constructor(
    private readonly fb: FormBuilder,
    private readonly store: Store<AuthRootState>,
    private readonly authService: AuthService,
    private readonly route: ActivatedRoute,
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }

  ngOnInit() {
    // TODO what to do with this setTimeout?
    setTimeout(() => {
      this.authService.isLoggedIn().subscribe((user: User | null) => {
        if (user) {
          const redirectTo =
            this.route.snapshot.queryParamMap.get('redirectTo') || undefined;
          this.store.dispatch(authActions.loginSuccess({ user, redirectTo }));
        } else {
          this.isAuthCheckInProgress.set(false);
        }
      });
    }, 200);
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const redirectTo =
        this.route.snapshot.queryParamMap.get('redirectTo') || undefined;
      console.log('Dispatching action', this.loginForm.value);
      this.store.dispatch(
        authActions.login({
          ...this.loginForm.value,
          redirectTo,
        }),
      );
    }
  }
}
