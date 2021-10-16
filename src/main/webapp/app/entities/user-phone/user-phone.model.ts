import { IUser } from 'app/entities/user/user.model';

export interface IUserPhone {
  id?: number;
  phoneNumber?: string;
  verified?: boolean | null;
  internalUser?: IUser | null;
}

export class UserPhone implements IUserPhone {
  constructor(public id?: number, public phoneNumber?: string, public verified?: boolean | null, public internalUser?: IUser | null) {
    this.verified = this.verified ?? false;
  }
}

export function getUserPhoneIdentifier(userPhone: IUserPhone): number | undefined {
  return userPhone.id;
}
