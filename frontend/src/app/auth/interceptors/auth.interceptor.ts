import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../auth.service';
import { catchError, retry, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  // Engelle herhangi bir CORS hatasına neden olabilecek istekleri
  const modifiedReq = req.clone({
    setHeaders: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    withCredentials: false // CORS sorunlarını önlemek için
  });

  // Token varsa ekle
  if (token) {
    const authReq = modifiedReq.clone({
      headers: modifiedReq.headers.set('Authorization', `Bearer ${token}`)
    });
    
    return next(authReq).pipe(
      retry(1), // Hata durumunda bir kez tekrar dene
      catchError((error: HttpErrorResponse) => {
        console.error('HTTP Error:', error);
        
        if (error.status === 401 || error.status === 403) {
          console.log('Authentication error, logging out');
          authService.logout();
        }
        
        // Kullanıcıya dost hata mesajları
        let errorMsg = 'Bir hata oluştu';
        if (error.error && typeof error.error.message === 'string') {
          // API'den gelen hata mesajı
          errorMsg = error.error.message;
        } else if (error.status === 0) {
          // Bağlantı hatası
          errorMsg = 'Sunucuya bağlanılamıyor. Lütfen internet bağlantınızı kontrol edin.';
        } else if (error.status === 404) {
          errorMsg = 'İstenen kaynak bulunamadı';
        } else if (error.status === 500) {
          errorMsg = 'Sunucu hatası';
        }
        
        return throwError(() => new Error(errorMsg));
      })
    );
  }
  
  return next(modifiedReq).pipe(
    retry(1),
    catchError((error: HttpErrorResponse) => {
      console.error('HTTP Error in non-auth request:', error);
      
      let errorMsg = 'Bir hata oluştu';
      if (error.error && typeof error.error.message === 'string') {
        errorMsg = error.error.message;
      } else if (error.status === 0) {
        errorMsg = 'Sunucuya bağlanılamıyor. Lütfen internet bağlantınızı kontrol edin.';
      }
      
      return throwError(() => new Error(errorMsg));
    })
  );
};
