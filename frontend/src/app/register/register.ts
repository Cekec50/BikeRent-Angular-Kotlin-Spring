import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { UserService } from '../service/user';
import { User } from '../model/UserModel';

@Component({
  selector: 'app-register',
  imports: [CommonModule, RouterLink, RouterLinkActive, FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  firstname = '';
  lastname = '';
  username = '';
  email = '';
  phone = '';
  password = '';
  debugMessage: string | null = null;

  constructor(
    private router: Router,
    private userService: UserService,
  ) {}

  register() {
    this.debugMessage = 'Sending register request...';

    const newUser: Omit<User, 'id'> = {
      username: this.username,
      password: this.password,
      firstName: this.firstname,
      lastName: this.lastname,
      phone: this.phone,
      email: this.email,
      isAdmin: true, // Web app is for admins; Android app sends false for regular users
    };

    this.userService.register(newUser).subscribe({
      next: (res) => {
        this.debugMessage = `Registration successful for ${res.username}`;
        console.log('Register response from backend:', res);
        // After successful registration, go to login (or bikes)
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.debugMessage = `Registration failed (status ${err.status})`;
        console.error('Register failed (backend error):', err);
      },
      complete: () => {
        this.debugMessage = (this.debugMessage || '') + ' (request completed)';
      },
    });
  }
}
