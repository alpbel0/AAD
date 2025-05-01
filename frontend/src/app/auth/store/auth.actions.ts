import { createAction, props } from '@ngrx/store';
import { User } from './auth.state';

// Login Actions
export const login = createAction(
  '[Auth] Login',
  props<{ email: string; password: string }>()
);

export const loginSuccess = createAction(
  '[Auth] Login Success',
  props<{ user: User; token: string }>()
);

export const loginFailure = createAction(
  '[Auth] Login Failure',
  props<{ error: string }>()
);

// Register Actions
export const register = createAction(
  '[Auth] Register',
  props<{ email: string; password: string; firstName: string; lastName: string }>()
);

export const registerSuccess = createAction(
  '[Auth] Register Success',
  props<{ user: User; token: string }>()
);

export const registerFailure = createAction(
  '[Auth] Register Failure',
  props<{ error: string }>()
);

// Clear Error Action
export const clearError = createAction('[Auth] Clear Error');

// Logout Action
export const logout = createAction('[Auth] Logout');

// Load User Action
export const loadUser = createAction('[Auth] Load User');
export const loadUserSuccess = createAction(
  '[Auth] Load User Success',
  props<{ user: User }>()
);
export const loadUserFailure = createAction(
  '[Auth] Load User Failure',
  props<{ error: string }>()
);
