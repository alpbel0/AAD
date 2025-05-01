import { createReducer, on } from '@ngrx/store';
import { AuthState } from './auth.state';
import * as AuthActions from './auth.actions';

export const initialState: AuthState = {
  user: null,
  token: null,
  isAuthenticated: false,
  loading: false,
  error: null
};

export const authReducer = createReducer(
  initialState,

  // Login
  on(AuthActions.login, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(AuthActions.loginSuccess, (state, { user, token }) => ({
    ...state,
    user,
    token,
    isAuthenticated: true,
    loading: false,
    error: null
  })),

  on(AuthActions.loginFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // Register
  on(AuthActions.register, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(AuthActions.registerSuccess, (state, { user, token }) => ({
    ...state,
    user,
    token,
    isAuthenticated: true,
    loading: false,
    error: null
  })),

  on(AuthActions.registerFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  })),

  // Clear Error
  on(AuthActions.clearError, (state) => ({
    ...state,
    error: null
  })),

  // Logout
  on(AuthActions.logout, () => ({
    ...initialState
  })),

  // Load User
  on(AuthActions.loadUser, (state) => ({
    ...state,
    loading: true,
    error: null
  })),

  on(AuthActions.loadUserSuccess, (state, { user }) => ({
    ...state,
    user,
    loading: false,
    error: null
  })),

  on(AuthActions.loadUserFailure, (state, { error }) => ({
    ...state,
    loading: false,
    error
  }))
);

// Reducer'ı named export olarak dışa aktar
export const reducer = authReducer;
