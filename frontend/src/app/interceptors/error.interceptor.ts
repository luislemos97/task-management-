import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private snack: MatSnackBar, private router: Router, private auth: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((err: HttpErrorResponse) => {
        // backend padroniza ApiError com field message
        const msg = (err.error && err.error.message) ? err.error.message : 'Ocorreu um erro na requisição.';

        if (err.status === 401) {
          this.snack.open('Sessão expirada. Faça login novamente.', 'Fechar', { duration: 3500 });
          this.auth.logout();
          return throwError(() => err);
        }

        this.snack.open(msg, 'Fechar', { duration: 3500 });
        return throwError(() => err);
      })
    );
  }
}
