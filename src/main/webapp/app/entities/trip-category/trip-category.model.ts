import { ITripdetail } from 'app/entities/tripdetail/tripdetail.model';

export interface ITripCategory {
  id?: number;
  categoryname?: string | null;
  tripdetails?: ITripdetail[] | null;
}

export class TripCategory implements ITripCategory {
  constructor(public id?: number, public categoryname?: string | null, public tripdetails?: ITripdetail[] | null) {}
}

export function getTripCategoryIdentifier(tripCategory: ITripCategory): number | undefined {
  return tripCategory.id;
}
