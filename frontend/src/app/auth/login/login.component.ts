import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { Store } from '@ngrx/store';
import { CommonModule } from '@angular/common';
import { Observable, Subscription, filter, take } from 'rxjs';
import * as AuthActions from '../store/auth.actions';
import * as AuthSelectors from '../store/auth.selectors';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit, OnDestroy {
  loginForm: FormGroup;
  loading$: Observable<boolean>;
  error$: Observable<string | null>;
  isSubmitting = false;
  private loadingSubscription?: Subscription;

  constructor(
    private fb: FormBuilder,
    private store: Store,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });

    this.loading$ = this.store.select(AuthSelectors.selectLoading);
    this.error$ = this.store.select(AuthSelectors.selectError);
  }

  ngOnInit(): void {
    // Form'u sıfırla
    this.loginForm.reset();

    // Varsa önceki hataları temizle
    this.store.dispatch(AuthActions.clearError());

    // Loading state dinle
    this.loadingSubscription = this.loading$.subscribe(loading => {
      if (!loading) {
        this.isSubmitting = false;
      }
    });
  }

  ngOnDestroy(): void {
    if (this.loadingSubscription) {
      this.loadingSubscription.unsubscribe();
    }
  }

  onSubmit(): void {
    // Form zaten gönderiliyorsa veya geçersizse işlem yapma
    if (this.isSubmitting || this.loginForm.invalid) {
      // Form alanlarını dokunulmuş olarak işaretle ki validasyon hataları görünsün
      if (this.loginForm.invalid) {
        Object.keys(this.loginForm.controls).forEach(key => {
          const control = this.loginForm.get(key);
          if (control?.invalid) {
            control.markAsTouched();
          }
        });
      }
      return;
    }

    // Form gönderiliyor olarak işaretle
    this.isSubmitting = true;

    // Önceki hataları temizle
    this.store.dispatch(AuthActions.clearError());

    // Login action'ı gönder
    const { email, password } = this.loginForm.value;
    this.store.dispatch(AuthActions.login({ email, password }));
  }
}
