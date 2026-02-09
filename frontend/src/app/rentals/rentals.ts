import { Component, OnInit } from '@angular/core';
import { Rental } from '../model/RentalModel';
import { CommonModule } from '@angular/common';
import { RentalService } from '../service/rental';

@Component({
  selector: 'app-rentals',
  imports: [CommonModule],
  templateUrl: './rentals.html',
  styleUrl: './rentals.css',
})
export class Rentals implements OnInit {
  rentals: Rental[] = [];
  errorMessage: string | null = null;

  constructor(private rentalService: RentalService) {}

  ngOnInit(): void {
    this.loadRentals();
  }

  loadRentals(): void {
    this.errorMessage = null;
    this.rentalService.getRentals().subscribe({
      next: (rentals) => {
        this.rentals = rentals;
      },
      error: (err) => {
        console.error('Failed to load rentals', err);
        this.errorMessage = 'Failed to load rentals';
        this.rentals = [];
      },
    });
  }
}
