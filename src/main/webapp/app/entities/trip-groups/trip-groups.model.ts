import { IUser } from 'app/entities/user/user.model';

export interface ITripGroups {
  id?: number;
  title?: string | null;
  description?: string | null;
  internalUsers?: IUser[] | null;
}

export class TripGroups implements ITripGroups {
  constructor(
    public id?: number,
    public title?: string | null,
    public description?: string | null,
    public internalUsers?: IUser[] | null
  ) {}
}

export function getTripGroupsIdentifier(tripGroups: ITripGroups): number | undefined {
  return tripGroups.id;
}
