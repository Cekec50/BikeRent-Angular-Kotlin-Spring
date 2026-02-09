import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Bike } from '../model/BikeModel';

@Injectable({
  providedIn: 'root',
})
export class BikeService {
  private readonly baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getBikes(): Observable<Bike[]> {
    return this.http.get<Bike[]>(`${this.baseUrl}/bikes`);
  }

  getBikeById(id: number): Observable<Bike> {
    return this.http.get<Bike>(`${this.baseUrl}/bikes/${id}`);
  }

  createBike(bike: Omit<Bike, 'id'>): Observable<Bike> {
    return this.http.post<Bike>(`${this.baseUrl}/bikes`, bike);
  }

  updateBike(id: number, bike: Bike): Observable<Bike> {
    return this.http.put<Bike>(`${this.baseUrl}/bikes/${id}`, bike);
  }
}

