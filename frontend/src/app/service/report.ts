import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Report } from '../model/ReportModel';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  private readonly baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getReports(): Observable<Report[]> {
    return this.http.get<Report[]>(`${this.baseUrl}/reports`);
  }

  deleteReport(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/reports/${id}`);
  }

  deleteReportsByBikeId(bikeId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/reports?bikeId=${bikeId}`);
  }
}
