import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Report } from '../model/ReportModel';
import { CommonModule } from '@angular/common';
import { ReportService } from '../service/report';
import { BikeService } from '../service/bike';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-photos',
  imports: [CommonModule, FormsModule],
  templateUrl: './reports.html',
  styleUrl: './reports.css',
})
export class Reports implements OnInit {
  reports: Report[] = [];
  filteredReports: Report[] = [];
  searchTerm: string = '';
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
        this.filteredReports = reports;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Failed to load reports', err);
        this.errorMessage = 'Failed to load reports';
        this.reports = [];
        this.filteredReports = [];
        this.cdr.markForCheck();
      },
    });
  }

  filterReports(): void {
    const term = this.searchTerm.toLowerCase();
    this.filteredReports = this.reports.filter((report) =>
      report.bikeId.toString().includes(term) ||
      report.description.toLowerCase().includes(term)
    );
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
                this.filterReports();
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
                this.filterReports();
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
