import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { User } from '../model/UserModel';
import { AuthService } from '../service/auth';
import { UserService } from '../service/user';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit {
  user: User | null = null;
  errorMessage: string | null = null;
  newPassword = '';
  confirmPassword = '';

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.user = this.authService.getCurrentUser();
    if (!this.user) {
      this.router.navigate(['/login']);
    }
  }

  saveChanges(): void {
    if (!this.user) return;
    if (this.newPassword !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match';
      return;
    }

    this.errorMessage = null;
    const payload: Partial<User> = {
      firstName: this.user.firstName,
      lastName: this.user.lastName,
      username: this.user.username,
      email: this.user.email,
      phone: this.user.phone,
    };
    if (this.newPassword) {
      payload.password = this.newPassword;
    }

    this.userService.updateUser(this.user.id, payload).subscribe({
      next: (updated) => {
        this.authService.setCurrentUser(updated);
        this.newPassword = '';
        this.confirmPassword = '';
        alert('Profile updated successfully.')
      },
      error: (err) => {
        console.error('Failed to update profile', err);
        this.errorMessage = 'Failed to save changes';
      },
    });
  }
}
