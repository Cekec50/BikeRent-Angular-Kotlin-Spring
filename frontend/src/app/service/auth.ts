import { Injectable } from '@angular/core';
import { User } from '../model/UserModel';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private currentUser: User | null = null;

  setCurrentUser(user: User | null): void {
    this.currentUser = user;
  }

  getCurrentUser(): User | null {
    return this.currentUser;
  }

  clearCurrentUser(): void {
    this.currentUser = null;
  }

  isLoggedIn(): boolean {
    return this.currentUser != null;
  }
}
