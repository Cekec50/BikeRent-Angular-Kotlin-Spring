import { Component, OnInit } from '@angular/core';
import { Rental } from '../model/RentalModel';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-rentals',
  imports: [CommonModule],
  templateUrl: './rentals.html',
  styleUrl: './rentals.css',
})
export class Rentals implements OnInit {
  rentals: Rental[] = [];

  ngOnInit(): void {
    this.loadRentals();
  }

  // Mock database fetch
  loadRentals(): void {
    this.rentals = [
      {
        bikeId: 102,
        user: {
          id: 1,
          username: 'mmarkeccc',
          password: '',
          firstname: 'Marko',
          lastname: 'Marković',
          phone: '+3816441214',
          email: 'markomarkovic@gmail.com',
          isAdmin: false,
        },
        startTime: '12.02.2026 10:15',
        endTime: '12.02.2026 14:45',
        totalPrice: 1200,
        photoUrl: 'assets/images/parked-bike.png',
      },
      {
        bikeId: 215,
        user: {
          id: 2,
          username: 'anapetrovic',
          password: '',
          firstname: 'Ana',
          lastname: 'Petrović',
          phone: '+381641234567',
          email: 'ana.petrovic@gmail.com',
          isAdmin: false,
        },
        startTime: '13.02.2026 09:30',
        endTime: '13.02.2026 11:10',
        totalPrice: 640,
        photoUrl: 'assets/images/parked-bike.png',
      },
      {
        bikeId: 102,
        user: {
          id: 1,
          username: 'mmarkeccc',
          password: '',
          firstname: 'Marko',
          lastname: 'Marković',
          phone: '+3816441214',
          email: 'markomarkovic@gmail.com',
          isAdmin: false,
        },
        startTime: '12.02.2026 10:15',
        endTime: '12.02.2026 14:45',
        totalPrice: 1200,
        photoUrl: 'assets/images/parked-bike.png',
      },
      {
        bikeId: 102,
        user: {
          id: 1,
          username: 'mmarkeccc',
          password: '',
          firstname: 'Marko',
          lastname: 'Marković',
          phone: '+3816441214',
          email: 'markomarkovic@gmail.com',
          isAdmin: false,
        },
        startTime: '12.02.2026 10:15',
        endTime: '12.02.2026 14:45',
        totalPrice: 1200,
        photoUrl: 'assets/images/parked-bike.png',
      },
      {
        bikeId: 102,
        user: {
          id: 1,
          username: 'mmarkeccc',
          password: '',
          firstname: 'Marko',
          lastname: 'Marković',
          phone: '+3816441214',
          email: 'markomarkovic@gmail.com',
          isAdmin: false,
        },
        startTime: '12.02.2026 10:15',
        endTime: '12.02.2026 14:45',
        totalPrice: 1200,
        photoUrl: 'assets/images/parked-bike.png',
      }
    ];
  }
}
