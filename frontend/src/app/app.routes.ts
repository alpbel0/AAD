import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { ProductListComponent } from './products/product-list/product-list.component';
import { ProductDetailComponent } from './products/product-detail/product-detail.component';
import { authGuard } from './auth/guards/auth.guard';
import { nonAuthGuard } from './auth/guards/non-auth.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  {
    path: 'auth/login',
    component: LoginComponent,
    canActivate: [nonAuthGuard]
  },
  {
    path: 'auth/register',
    component: RegisterComponent,
    canActivate: [nonAuthGuard]
  },
  {
    path: 'products',
    component: ProductListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'products/:id',
    component: ProductDetailComponent,
    canActivate: [authGuard]
  },
  { path: '**', redirectTo: '' }
];
