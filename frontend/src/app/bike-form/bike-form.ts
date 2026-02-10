import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Bike } from '../model/BikeModel';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BikeService } from '../service/bike';
import { GeocodingService } from '../service/geocoding';

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
    latitude: 0,
    longitude: 0,
    status: 1,
  };
  bikeId?: number;
  isEditMode = false;
  errorMessage: string | null = null;
  /** Address string for geocoding; user enters this and clicks Look up */
  locationInput = '';
  /** Set after a successful lookup; shown under the input */
  lastLookedUpAddress: string | null = null;
  isLookingUp = false;
  /** True after user clicks Look up (or when loading a bike that has coordinates) */
  locationSet = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private bikeService: BikeService,
    private geocodingService: GeocodingService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    const bikeIdParam = this.route.snapshot.paramMap.get('id');
    if (bikeIdParam) {
      this.isEditMode = true;
      this.bikeId = +bikeIdParam;
      this.loadBike(this.bikeId);
    } else {
      this.isEditMode = false;
      this.bikeId = undefined;
    }
  }

  loadBike(id: number): void {
    this.bikeService.getBikeById(id).subscribe({
      next: (bike) => {
        const raw = bike as Bike & { location?: string };
        this.bike = {
          id: bike.id,
          type: bike.type,
          price: bike.price,
          location: (raw as Bike & { location?: string }).location ?? '',
          status: bike.status === -1 || bike.status === 0 || bike.status === 1 ? bike.status : 1,
          latitude: Number(raw.latitude ?? 0),
          longitude: Number(raw.longitude ?? 0),
        };
        this.bikeId = this.bike.id;
        this.locationInput = this.bike.location;
        this.lastLookedUpAddress = this.bike.location || null;
        this.locationSet = true; // we have coordinates from backend
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Failed to load bike', err);
        this.errorMessage = 'Failed to load bike data';
      },
    });
  }

  lookUpAddress(): void {
    const query = this.locationInput?.trim();
    if (!query) {
      this.errorMessage = 'Please enter a location.';
      return;
    }
    this.errorMessage = null;
    this.isLookingUp = true;
    this.geocodingService.geocode(query).subscribe({
      next: (result) => {
        this.bike.latitude = result.lat;
        this.bike.longitude = result.lon;
        this.bike.location = result.displayName ?? query;
        this.lastLookedUpAddress = this.bike.location;
        this.locationSet = true;
        this.isLookingUp = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.isLookingUp = false;
        this.errorMessage = err?.message ?? 'Address not found. Try a different search.';
        this.cdr.markForCheck();
      },
    });
  }

  saveBike(): void {
    const { type, latitude, longitude } = this.bike;
    if (!type?.trim()) {
      this.errorMessage = 'Please fill in the type.';
      return;
    }
    if (!this.locationSet) {
      this.errorMessage = 'Please enter a location and click Look up to set coordinates (latitude/longitude).';
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
