export interface Report {
  id: number;
  bikeId: number;
  description: string;
  photoUrl: string;
  status: 'OPEN' | 'IN_REPAIR' | 'RESOLVED';
}
