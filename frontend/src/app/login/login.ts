import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { UserService } from '../service/user';
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
  ) {}

  login() {
    console.log('Sending login request with:', {
      username: this.username,
      password: this.password ? '***' : '(empty)',
    });

    this.userService
      .login({ username: this.username, password: this.password })
      .subscribe({
        next: (response) => {
          console.log('Login response from backend:', response);
          this.router.navigate(['/bikes']);
        },
        error: (error) => {
          console.error('Login failed (backend error):', error);
          // TODO: show an error message to the user
        },
        complete: () => {
          console.log('Login request completed');
        },
      });
  }
}
