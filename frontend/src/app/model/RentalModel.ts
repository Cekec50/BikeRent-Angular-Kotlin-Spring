import { Bike } from "./BikeModel";
import { User } from "./UserModel";

export interface Rental {
  bike: Bike;
  user: User;
  startTime: string;
  endTime: string;
  totalPrice: number;
  photoUrl: string;
}