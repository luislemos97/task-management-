import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { map, tap } from 'rxjs/operators';
import { TokenResponse } from '../models/auth';

const TOKEN_KEY = 'tm_token';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private http: HttpClient, private router: Router) {}

  get token(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    return !!this.token;
  }

  login(username: string, password: string) {
    return this.http.post<TokenResponse>('/api/auth/login', { username, password }).pipe(
      tap(res => {
        if (res.token) localStorage.setItem(TOKEN_KEY, res.token);
      }),
      map(res => !!res.token)
    );
  }

  register(username: string, email: string, password: string) {
    return this.http.post('/api/auth/register', { username, email, password });
  }

  logout() {
    localStorage.removeItem(TOKEN_KEY);
    this.router.navigateByUrl('/login');
  }
}
