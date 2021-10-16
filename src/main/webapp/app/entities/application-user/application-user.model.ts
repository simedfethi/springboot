import { IUser } from 'app/entities/user/user.model';

export interface IApplicationUser {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string | null;
  address?: string | null;
  phone?: string | null;
  mobilePhone?: string | null;
  internalUser?: IUser | null;
}

export class ApplicationUser implements IApplicationUser {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public email?: string | null,
    public address?: string | null,
    public phone?: string | null,
    public mobilePhone?: string | null,
    public internalUser?: IUser | null
  ) {}
}

export function getApplicationUserIdentifier(applicationUser: IApplicationUser): number | undefined {
  return applicationUser.id;
}
