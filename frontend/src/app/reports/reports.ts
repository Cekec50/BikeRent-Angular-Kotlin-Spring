import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Report } from '../model/ReportModel';
import { CommonModule } from '@angular/common';
import { ReportService } from '../service/report';
import { BikeService } from '../service/bike';

@Component({
  selector: 'app-photos',
  imports: [CommonModule],
  templateUrl: './reports.html',
  styleUrl: './reports.css',
})
export class Reports implements OnInit {
  reports: Report[] = [];
  errorMessage: string | null = null;

  constructor(
    private reportService: ReportService,
    private bikeService: BikeService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.loadReports();
  }

  loadReports(): void {
    this.errorMessage = null;
    this.reportService.getReports().subscribe({
      next: (reports) => {
        this.reports = reports;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Failed to load reports', err);
        this.errorMessage = 'Failed to load reports';
        this.reports = [];
        this.cdr.markForCheck();
      },
    });
  }

  sendBikeToRepair(report: Report): void {
    this.errorMessage = null;

    // Step 1: get the bike from the backend
    this.bikeService.getBikeById(report.bikeId).subscribe({
      next: (bike) => {
        // Step 2: set bike status to 0 (in repair) and save
        const bikeInRepair = { ...bike, status: 0 as const };
        this.bikeService.updateBike(bike.id, bikeInRepair).subscribe({
          next: () => {
            // Step 3: delete all reports for this bike (one request)
            this.reportService.deleteReportsByBikeId(report.bikeId).subscribe({
              next: () => {
                this.reports = this.reports.filter((r) => r.bikeId !== report.bikeId);
                this.cdr.markForCheck();
              },
              error: (err) => {
                console.error('Failed to delete reports', err);
                this.errorMessage = 'Failed to send bike to repair';
                this.cdr.markForCheck();
              },
            });
          },
          error: (err) => {
            console.error('Failed to update bike status', err);
            this.errorMessage = 'Failed to send bike to repair';
            this.cdr.markForCheck();
          },
        });
      },
      error: (err) => {
        console.error('Failed to load bike', err);
        this.errorMessage = 'Failed to send bike to repair';
        this.cdr.markForCheck();
      },
    });
  }

  markBikeAsRemoved(report: Report): void {
    this.errorMessage = null;

    // Step 1: get the bike from the backend
    this.bikeService.getBikeById(report.bikeId).subscribe({
      next: (bike) => {
        // Step 2: set bike status to -1 (removed) and save
        const bikeRemoved = { ...bike, status: -1 as const };
        this.bikeService.updateBike(bike.id, bikeRemoved).subscribe({
          next: () => {
            // Step 3: delete all reports for this bike (one request)
            this.reportService.deleteReportsByBikeId(report.bikeId).subscribe({
              next: () => {
                this.reports = this.reports.filter((r) => r.bikeId !== report.bikeId);
                this.cdr.markForCheck();
              },
              error: (err) => {
                console.error('Failed to delete reports', err);
                this.errorMessage = 'Failed to mark bike as removed';
                this.cdr.markForCheck();
              },
            });
          },
          error: (err) => {
            console.error('Failed to update bike status', err);
            this.errorMessage = 'Failed to mark bike as removed';
            this.cdr.markForCheck();
          },
        });
      },
      error: (err) => {
        console.error('Failed to load bike', err);
        this.errorMessage = 'Failed to mark bike as removed';
        this.cdr.markForCheck();
      },
    });
  }
}
