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

  constructor(
    private router: Router,
    private userService: UserService,
    private authService: AuthService,
  ) {}

  login() {
    this.userService
      .login({ username: this.username, password: this.password })
      .subscribe({
        next: (user) => {
          this.authService.setCurrentUser(user);
          this.router.navigate(['/bikes']);
        },
        error: (error) => {
          console.error('Login failed (backend error):', error);
          // TODO: show an error message to the user
        },
      });
  }
}
