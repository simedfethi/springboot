import { IUser } from 'app/entities/user/user.model';

export interface INotifications {
  id?: number;
  title?: string | null;
  content?: string | null;
  user?: IUser | null;
}

export class Notifications implements INotifications {
  constructor(public id?: number, public title?: string | null, public content?: string | null, public user?: IUser | null) {}
}

export function getNotificationsIdentifier(notifications: INotifications): number | undefined {
  return notifications.id;
}
