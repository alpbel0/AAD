import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';

import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { AuthEffects } from './store/auth.effects';
import { authReducer } from './store/auth.reducer';

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    StoreModule.forFeature('auth', authReducer),
    EffectsModule.forFeature([AuthEffects]),
    LoginComponent,
    RegisterComponent
  ]
})
export class AuthModule { }
