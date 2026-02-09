import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Bike } from '../model/BikeModel';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BikeService } from '../service/bike';

@Component({
  selector: 'app-bike-form',
  imports: [FormsModule, CommonModule],
  templateUrl: './bike-form.html',
  styleUrl: './bike-form.css',
})
export class BikeForm implements OnInit {
  bike: Bike = {
    id: 0,
    type: '',
    price: 0,
    location: '',
    status: 1,
  };
  bikeId?: number;
  isEditMode = false;
  errorMessage: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private bikeService: BikeService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const bikeIdParam = this.route.snapshot.paramMap.get('id');
    if (bikeIdParam) {
      // Edit mode: fetch bike data from backend
      this.isEditMode = true;
      this.bikeId = +bikeIdParam;
      this.loadBike(this.bikeId);
    } else {
      // Add mode: initialize empty bike
      this.isEditMode = false;
      this.bikeId = undefined;
    }
  }

  loadBike(id: number): void {
    this.bikeService.getBikeById(id).subscribe({
      next: (bike) => {
        this.bike = bike;
        this.bikeId = bike.id;  
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to load bike', err);
        this.errorMessage = 'Failed to load bike data';
      },
    });
  }

  saveBike(): void {
    if (!this.bike.type || !this.bike.location) {
      this.errorMessage = 'Please fill in all required fields';
      return;
    }

    this.errorMessage = null;

    if (this.isEditMode && this.bikeId) {
      // Update existing bike
      this.bikeService.updateBike(this.bikeId, this.bike).subscribe({
        next: () => {
          console.log('Bike updated successfully');
          this.router.navigate(['/bikes']);
        },
        error: (err) => {
          console.error('Failed to update bike', err);
          this.errorMessage = 'Failed to update bike';
        },
      });
    } else {
      // Create new bike
      const { id, ...newBike } = this.bike;
      this.bikeService.createBike(newBike).subscribe({
        next: () => {
          console.log('Bike created successfully');
          this.router.navigate(['/bikes']);
        },
        error: (err) => {
          console.error('Failed to create bike', err);
          this.errorMessage = 'Failed to create bike';
        },
      });
    }
  }
}
