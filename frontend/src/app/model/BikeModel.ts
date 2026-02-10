export interface Bike {
  id: number;
  type: string;
  price: number;
  location: string;
  latitude: number;
  longitude: number;
  status: -1 | 0 | 1;
}
