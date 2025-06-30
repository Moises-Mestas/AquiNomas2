import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.services';

@Component({
  selector: 'app-auth',
  standalone: true,
  templateUrl: './auth.page.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./auth.page.css'],
})
export class AuthPage {
  userName = '';
  password = '';
  errorMsg = '';

  constructor(private authService: AuthService) {}

  login() {
    this.errorMsg = '';
    this.authService.login(this.userName, this.password).subscribe({
      next: (res) => {
        localStorage.setItem('access_token', res.token);
        // Redirige a la página principal, ejemplo:
        // window.location.href = '/productos';
      },
      error: (err) => {
        this.errorMsg = 'Credenciales incorrectas';
      },
    });
  }
}
