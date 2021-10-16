import { IUser } from 'app/entities/user/user.model';
import { GovernmentIDType } from 'app/entities/enumerations/government-id-type.model';

export interface IUserGovernmentID {
  id?: number;
  country?: string | null;
  docType?: GovernmentIDType | null;
  docPicRectContentType?: string | null;
  docPicRect?: string | null;
  docPicVersContentType?: string | null;
  docPicVers?: string | null;
  docPicInstContentType?: string | null;
  docPicInst?: string | null;
  idVerified?: boolean | null;
  user?: IUser | null;
}

export class UserGovernmentID implements IUserGovernmentID {
  constructor(
    public id?: number,
    public country?: string | null,
    public docType?: GovernmentIDType | null,
    public docPicRectContentType?: string | null,
    public docPicRect?: string | null,
    public docPicVersContentType?: string | null,
    public docPicVers?: string | null,
    public docPicInstContentType?: string | null,
    public docPicInst?: string | null,
    public idVerified?: boolean | null,
    public user?: IUser | null
  ) {
    this.idVerified = this.idVerified ?? false;
  }
}

export function getUserGovernmentIDIdentifier(userGovernmentID: IUserGovernmentID): number | undefined {
  return userGovernmentID.id;
}
