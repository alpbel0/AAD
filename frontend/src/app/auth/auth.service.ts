import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { catchError, delay, retry, tap, timeout } from 'rxjs/operators';
import { isPlatformBrowser } from '@angular/common';

interface AuthResponse {
  token: string;
  user: {
    id: number;
    email: string;
    firstName: string;
    lastName: string;
    role: string;
  }
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private tokenKey = 'auth_token';
  private userKey = 'user_info';
  private isBrowser: boolean;
  private connectionTimeout = 15000; // 15 saniye timeout

  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  private currentUserSubject = new BehaviorSubject<any>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(this.platformId);
    if (this.isBrowser) {
      this.isAuthenticatedSubject.next(this.hasToken());
      this.currentUserSubject.next(this.getUserFromStorage());
    }
    console.log('AuthService initialized, isBrowser:', this.isBrowser);
  }

  login(email: string, password: string): Observable<AuthResponse> {
    console.log('Login attempt for:', email);

    // Backend'in beklediği formatta veri gönderiyoruz
    const loginData = {
      email: email,
      password: password,
      rememberMe: true  // Backend'de bu alan tanımlı
    };

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      })
    };

    return this.http.post<AuthResponse>(
      `${this.apiUrl}/login`,
      loginData,
      httpOptions
    ).pipe(
      timeout(this.connectionTimeout),
      retry(2),
      tap(response => {
        console.log('Login response received:', response);
        this.handleAuthResponse(response);
      }),
      catchError(this.handleError('login'))
    );
  }

  register(userData: { email: string; password: string; firstName: string; lastName: string }): Observable<AuthResponse> {
    console.log('Register attempt for:', userData.email);
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      })
    };

    return this.http.post<AuthResponse>(
      `${this.apiUrl}/register`,
      userData,
      httpOptions
    ).pipe(
      timeout(this.connectionTimeout),
      retry(2),
      tap(response => {
        console.log('Register response received:', response);
        this.handleAuthResponse(response);
      }),
      catchError(this.handleError('register'))
    );
  }

  logout(): void {
    console.log('Logging out user');
    if (this.isBrowser) {
      localStorage.removeItem(this.tokenKey);
      localStorage.removeItem(this.userKey);
    }
    this.isAuthenticatedSubject.next(false);
    this.currentUserSubject.next(null);
  }

  private handleAuthResponse(response: AuthResponse): void {
    if (!response || !response.token) {
      console.error('Invalid auth response:', response);
      throw new Error('Sunucudan geçersiz yanıt alındı');
    }

    if (this.isBrowser) {
      console.log('Saving auth data to localStorage');
      localStorage.setItem(this.tokenKey, response.token);
      localStorage.setItem(this.userKey, JSON.stringify(response.user));
    }

    this.isAuthenticatedSubject.next(true);
    this.currentUserSubject.next(response.user);
  }

  getToken(): string | null {
    if (!this.isBrowser) {
      return null;
    }
    return localStorage.getItem(this.tokenKey);
  }

  private hasToken(): boolean {
    return !!this.getToken();
  }

  private getUserFromStorage(): any {
    if (!this.isBrowser) {
      return null;
    }
    const userStr = localStorage.getItem(this.userKey);
    return userStr ? JSON.parse(userStr) : null;
  }

  private handleError<T>(operation = 'operation') {
    return (error: HttpErrorResponse): Observable<never> => {
      console.error(`${operation} error:`, error);

      let errorMessage = 'Bir hata oluştu';

      if (error.error instanceof ErrorEvent) {
        // Client-side error
        errorMessage = `Hata: ${error.error.message}`;
      } else if (error.status === 0) {
        // Network error
        errorMessage = 'Sunucuya bağlanılamıyor. Lütfen internet bağlantınızı kontrol edin.';
      } else {
        // Server error with a message
        if (error.error && typeof error.error === 'object' && error.error.message) {
          errorMessage = error.error.message;
        } else if (typeof error.error === 'string' && error.error) {
          try {
            const parsedError = JSON.parse(error.error);
            if (parsedError.message) {
              errorMessage = parsedError.message;
            }
          } catch (e) {
            // If error is not JSON, use it directly
            errorMessage = error.error;
          }
        } else {
          // Standard HTTP errors
          switch (error.status) {
            case 400: errorMessage = 'Geçersiz istek'; break;
            case 401: errorMessage = 'Giriş yapmanız gerekiyor'; break;
            case 403: errorMessage = 'Bu işlem için yetkiniz yok'; break;
            case 404: errorMessage = 'Kaynak bulunamadı'; break;
            case 422: errorMessage = 'Geçersiz veri'; break;
            case 500: errorMessage = 'Sunucu hatası'; break;
            default: errorMessage = `Sunucu hatası: ${error.status}`; break;
          }
        }
      }

      return throwError(() => new Error(errorMessage));
    };
  }
}
