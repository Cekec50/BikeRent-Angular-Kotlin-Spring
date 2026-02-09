import { Component, OnInit } from '@angular/core';
import { Report } from '../model/ReportModel';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-photos',
  imports: [CommonModule],
  templateUrl: './reports.html',
  styleUrl: './reports.css',
})
export class Reports implements OnInit {
  reports: Report[] = [];

  ngOnInit(): void {
    this.loadReports();
  }

  // Mock DB fetch
  loadReports(): void {
    this.reports = [
      {
        id: 1,
        bikeId: 1,
        description: 'The tire is flat',
        photoUrl: 'assets/images/flat-tire.png',
        status: 'OPEN',
      },
      {
        id: 2,
        bikeId: 2,
        description: 'Wheel is broken',
        photoUrl: 'assets/images/flat-tire.png',
        status: 'OPEN',
      },
      {
        id: 2,
        bikeId: 2,
        description: 'Wheel is broken',
        photoUrl: 'assets/images/flat-tire.png',
        status: 'OPEN',
      },
      {
        id: 2,
        bikeId: 2,
        description: 'Wheel is broken',
        photoUrl: 'assets/images/flat-tire.png',
        status: 'OPEN',
      },
      {
        id: 2,
        bikeId: 2,
        description: 'Wheel is broken',
        photoUrl: 'assets/images/flat-tire.png',
        status: 'OPEN',
      }
    ];
  }

  sendToRepair(report: Report): void {
    report.status = 'IN_REPAIR';
  }

  removeReport(reportId: number): void {
    this.reports = this.reports.filter(r => r.id !== reportId);
  }
}
