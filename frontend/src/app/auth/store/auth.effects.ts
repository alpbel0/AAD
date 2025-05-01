import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { map, mergeMap, catchError, tap, filter } from 'rxjs/operators';
import { Router } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';
import { AuthService } from '../auth.service';
import * as AuthActions from './auth.actions';

@Injectable()
export class AuthEffects {
  private isBrowser: boolean;
  login$;
  register$;
  authSuccess$;
  logout$;

  constructor(
    private actions$: Actions,
    private authService: AuthService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);

    this.login$ = createEffect(() =>
      this.actions$.pipe(
        ofType(AuthActions.login),
        filter(() => this.isBrowser),
        mergeMap(({ email, password }) =>
          this.authService.login(email, password).pipe(
            map(response => AuthActions.loginSuccess({ user: response.user, token: response.token })),
            catchError(error => {
              console.error('Login error:', error);
              const errorMessage = error.message || 'Giriş yapılırken bir hata oluştu';
              return of(AuthActions.loginFailure({ error: errorMessage }));
            })
          )
        )
      )
    );

    this.register$ = createEffect(() =>
      this.actions$.pipe(
        ofType(AuthActions.register),
        filter(() => this.isBrowser),
        mergeMap(action =>
          this.authService.register(action).pipe(
            map(response => AuthActions.registerSuccess({ user: response.user, token: response.token })),
            catchError(error => {
              console.error('Register error:', error);
              const errorMessage = error.message || 'Kayıt olurken bir hata oluştu';
              return of(AuthActions.registerFailure({ error: errorMessage }));
            })
          )
        )
      )
    );

    this.authSuccess$ = createEffect(
      () => this.actions$.pipe(
        ofType(AuthActions.loginSuccess, AuthActions.registerSuccess),
        filter(() => this.isBrowser),
        tap(() => this.router.navigate(['/']))
      ),
      { dispatch: false }
    );

    this.logout$ = createEffect(
      () => this.actions$.pipe(
        ofType(AuthActions.logout),
        filter(() => this.isBrowser),
        tap(() => { this.authService.logout(); this.router.navigate(['/auth/login']); })
      ),
      { dispatch: false }
    );
  }
}
