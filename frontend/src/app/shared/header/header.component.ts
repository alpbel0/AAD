import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Store } from '@ngrx/store';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';
import * as AuthSelectors from '../../auth/store/auth.selectors';
import * as AuthActions from '../../auth/store/auth.actions';
import { User } from '../../auth/store/auth.state';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  isAuthenticated$: Observable<boolean>;
  currentUser$: Observable<User | null>;

  constructor(private store: Store) {
    this.isAuthenticated$ = this.store.select(AuthSelectors.selectIsAuthenticated);
    this.currentUser$ = this.store.select(AuthSelectors.selectUser);
  }

  logout(): void {
    this.store.dispatch(AuthActions.logout());
  }
}
