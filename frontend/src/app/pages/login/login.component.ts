import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);
  private snack = inject(MatSnackBar);

  submitting = false;
  hidePassword = true;

  form = this.fb.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required]],
  });

  onSubmit() {
    if (this.form.invalid || this.submitting) return;

    this.submitting = true;
    const { username, password } = this.form.getRawValue();

    this.auth.login(username!, password!).subscribe({
      next: (ok) => {
        this.submitting = false;
        if (ok) this.router.navigateByUrl('/');
        else this.snack.open('Usuário ou senha inválidos.', 'Fechar', { duration: 3000 });
      },
      error: () => {
        this.submitting = false;
        this.snack.open('Não foi possível autenticar. Tente novamente.', 'Fechar', { duration: 3000 });
      },
    });
  }
}
