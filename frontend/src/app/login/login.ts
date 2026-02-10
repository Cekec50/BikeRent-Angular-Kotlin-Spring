import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { UserService } from '../service/user';
import { AuthService } from '../service/auth';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [CommonModule, RouterLink, RouterLinkActive, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  username = '';
  password = '';
  errorMessage: string | null = null;

  constructor(
    private router: Router,
    private userService: UserService,
    private authService: AuthService,
  ) {}

  login() {
    this.errorMessage = null;
    this.userService
      .login({ username: this.username, password: this.password, isAdmin: true })
      .subscribe({
        next: (user) => {
          this.authService.setCurrentUser(user);
          this.router.navigate(['/bikes']);
        },
        error: (error) => {
          console.error('Login failed:', error);
          // 403 = backend rejected because user is not admin
          if (error.status === 403) {
            this.errorMessage = 'Only administrators can access this app.';
          } else {
            this.errorMessage = 'Invalid username or password.';
          }
        },
      });
  }
}
