import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { map, take } from 'rxjs/operators';

export const nonAuthGuard = () => {
  const router = inject(Router);
  const authService = inject(AuthService);

  return authService.isAuthenticated$.pipe(
    take(1),
    map(isAuthenticated => {
      if (!isAuthenticated) {
        return true;
      } else {
        router.navigate(['/']);
        return false;
      }
    })
  );
};
