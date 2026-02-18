import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Bike } from '../model/BikeModel';
import { BikeService } from '../service/bike';

@Component({
  selector: 'app-bikes',
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './bikes.html',
  styleUrl: './bikes.css',
})
export class Bikes implements OnInit {
  bikes: Bike[] = [];
  filteredBikes: Bike[] = [];
  searchTerm: string = '';

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
        this.filteredBikes = bikes;
        this.cdr.detectChanges(); // Explicitly trigger change detection
      },
      error: (err) => {
        console.error('Failed to load bikes from backend', err);
      },
    });
  }

  filterBikes(): void {
    const term = this.searchTerm.toLowerCase();
    this.filteredBikes = this.bikes.filter((bike) =>
      bike.type.toLowerCase().includes(term) ||
      bike.id.toString().includes(term) || 
      bike.price.toString().includes(term) || 
      this.getStatusLabel(bike.status).toLowerCase().includes(term) 
    );
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
