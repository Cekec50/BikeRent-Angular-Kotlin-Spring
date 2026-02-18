import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';

export interface GeocodingResult {
  lat: number;
  lon: number;
  displayName?: string;
}

const NOMINATIM_URL = 'https://nominatim.openstreetmap.org/search';

/** Response from Nominatim API (single result). */
interface NominatimPlace {
  lat: string;
  lon: string;
  display_name?: string;
  address?: {
    road?: string;
    pedestrian?: string;
    house_number?: string;
    city?: string;
    town?: string;
    village?: string;
    hamlet?: string;
  };
}

@Injectable({
  providedIn: 'root',
})
export class GeocodingService {
  constructor(private http: HttpClient) {}

  /* Geocode an address string to latitude and longitude using Nominatim (OpenStreetMap). */
  
  geocode(address: string): Observable<GeocodingResult> {
    if (!address?.trim()) {
      throw new Error('Address is required');
    }
    const params = new HttpParams()
      .set('q', address.trim())
      .set('format', 'json')
      .set('limit', '1')
      .set('addressdetails', '1');
    const headers = {
      'User-Agent': 'BikeRent/1.0 (Angular admin app; contact@example.com)',
    };
    return this.http
      .get<NominatimPlace[]>(NOMINATIM_URL, { params, headers })
      .pipe(
        map((results) => {
          if (!results?.length) {
            throw new Error('Address not found');
          }
          const first = results[0];
          let displayName = first.display_name;

          if (first.address) {
            const road = first.address.road || first.address.pedestrian;
            const city = first.address.city || first.address.town || first.address.village || first.address.hamlet;
            if (road) {
              displayName = `${road} ${first.address.house_number || ''}`.trim();
              if (city) {
                displayName += `, ${city}`;
              }
            }
          }

          return {
            lat: Number(first.lat),
            lon: Number(first.lon),
            displayName,
          };
        }),
      );
  }
}
