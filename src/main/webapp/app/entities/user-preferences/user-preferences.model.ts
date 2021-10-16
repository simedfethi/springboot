import { IUser } from 'app/entities/user/user.model';

export interface IUserPreferences {
  id?: number;
  language?: string;
  currency?: string;
  timeZone?: string | null;
  internalUser?: IUser | null;
}

export class UserPreferences implements IUserPreferences {
  constructor(
    public id?: number,
    public language?: string,
    public currency?: string,
    public timeZone?: string | null,
    public internalUser?: IUser | null
  ) {}
}

export function getUserPreferencesIdentifier(userPreferences: IUserPreferences): number | undefined {
  return userPreferences.id;
}
