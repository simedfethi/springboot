import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { ITripCategory } from 'app/entities/trip-category/trip-category.model';

export interface ITripdetail {
  id?: number;
  minimumList?: number | null;
  maximumList?: number | null;
  createdDate?: dayjs.Dayjs | null;
  lastupdated?: dayjs.Dayjs | null;
  departureDate?: dayjs.Dayjs | null;
  arrivalDate?: dayjs.Dayjs | null;
  contentDate?: dayjs.Dayjs | null;
  tripmaster?: IUser | null;
  category?: ITripCategory | null;
}

export class Tripdetail implements ITripdetail {
  constructor(
    public id?: number,
    public minimumList?: number | null,
    public maximumList?: number | null,
    public createdDate?: dayjs.Dayjs | null,
    public lastupdated?: dayjs.Dayjs | null,
    public departureDate?: dayjs.Dayjs | null,
    public arrivalDate?: dayjs.Dayjs | null,
    public contentDate?: dayjs.Dayjs | null,
    public tripmaster?: IUser | null,
    public category?: ITripCategory | null
  ) {}
}

export function getTripdetailIdentifier(tripdetail: ITripdetail): number | undefined {
  return tripdetail.id;
}
