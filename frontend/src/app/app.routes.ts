import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Bikes } from './bikes/bikes';
import { MainLayout } from './layouts/main-layout/main-layout';
import { AuthLayout } from './layouts/auth-layout/auth-layout';
import { Rentals } from './rentals/rentals';
import { Reports } from './reports/reports';
import { Register } from './register/register';
import { Profile } from './profile/profile';
import { BikeForm } from './bike-form/bike-form';

export const routes: Routes = [
    {
      path: '',
      component: AuthLayout,
      children: [
        { path: '', redirectTo: 'login', pathMatch: 'full' },
        { path: 'login', component: Login },
        { path: 'register', component: Register }
      ]
    },
    {
    path: '',
    component: MainLayout,
    children: [
      { path: 'bikes', component: Bikes },
      { path: 'rentals', component: Rentals },
      { path: 'reports', component: Reports },
      { path: 'profile', component: Profile },
      { path: 'bike-form', component: BikeForm },           // Add
      { path: 'bike-form/:id', component: BikeForm },      // Edit
    ]
  }
];
