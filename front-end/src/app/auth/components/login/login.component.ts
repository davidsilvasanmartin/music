import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { Store } from '@ngrx/store';

import * as authActions from '../../store/actions';
import { AuthRootState } from '../../store/state';

@Component({
  selector: 'app-login',
  templateUrl: 'login.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(
    private readonly fb: FormBuilder,
    private readonly store: Store<AuthRootState>,
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      console.log('Dispatching action', this.loginForm.value);
      this.store.dispatch(authActions.login(this.loginForm.value));
    }
  }
}
