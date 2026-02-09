import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Bike } from '../model/BikeModel';
import { BikeService } from '../service/bike';

@Component({
  selector: 'app-bikes',
  imports: [CommonModule, RouterLink],
  templateUrl: './bikes.html',
  styleUrl: './bikes.css',
})
export class Bikes implements OnInit {
  bikes: Bike[] = [];

  constructor(
    private bikeService: BikeService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.loadBikes();
  }

  loadBikes(): void {
    this.bikeService.getBikes().subscribe({
      next: (bikes) => {
        console.log('Fetching bikes', bikes);
        this.bikes = bikes;
        this.cdr.detectChanges(); // Explicitly trigger change detection
      },
      error: (err) => {
        console.error('Failed to load bikes from backend', err);
      },
    });
  }

  editBike(bikeId: number): void {
    console.log('Edit bike with id:', bikeId);
    // later → navigate to /bikes/:id/edit
  }

  getStatusLabel(status: -1 | 0 | 1): string {
    switch (status) {
      case 1:
        return 'Available';
      case 0:
        return 'Unavailable';
      case -1:
        return 'Removed';
      default:
        return 'Unknown';
    }
  }
}

