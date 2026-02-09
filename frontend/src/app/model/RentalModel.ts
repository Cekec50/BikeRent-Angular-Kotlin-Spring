import { User } from "./UserModel";

export interface Rental {
  bikeId: number;
  user: User;
  startTime: string;
  endTime: string;
  totalPrice: number;
  photoUrl: string;
}