import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { User } from '../model/UserModel';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  login(credentials: { username: string; password: string }): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/login`, credentials).pipe(
      tap({
        next: (res) => console.log('Raw login response in service:', res),
        error: (err) => console.error('Login HTTP error in service:', err),
      }),
    );
  }

  register(user: Omit<User, 'id'>): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}/register`, user).pipe(
      tap({
        next: (res) => console.log('Raw register response in service:', res),
        error: (err) => console.error('Register HTTP error in service:', err),
      }),
    );
  }
}
