import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Rental } from '../model/RentalModel';
import { CommonModule } from '@angular/common';
import { RentalService } from '../service/rental';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-rentals',
  imports: [CommonModule, FormsModule],
  templateUrl: './rentals.html',
  styleUrl: './rentals.css',
})
export class Rentals implements OnInit {
  rentals: Rental[] = [];
  filteredRentals: Rental[] = [];
  searchTerm: string = '';
  errorMessage: string | null = null;

  constructor(private rentalService: RentalService,
    private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.loadRentals();
  }

  loadRentals(): void {
    this.errorMessage = null;
    this.rentalService.getRentals().subscribe({
      next: (rentals: Rental[]) => {
        this.rentals = rentals;
        this.filteredRentals = rentals;
        this.cdr.detectChanges();
      },
      error: (err: unknown) => {
        console.error('Failed to load rentals', err);
        this.errorMessage = 'Failed to load rentals';
        this.rentals = [];
        this.filteredRentals = [];
      },
    });
  }

  filterRentals(): void {
    const term = this.searchTerm.toLowerCase();
    this.filteredRentals = this.rentals.filter((rental) =>
      rental.bike.id.toString().includes(term) ||
      (rental.user.firstName + ' ' + rental.user.lastName).toLowerCase().includes(term) ||
      rental.user.username.toLowerCase().includes(term) ||
      rental.user.email.toLowerCase().includes(term)
    );
  }
}
