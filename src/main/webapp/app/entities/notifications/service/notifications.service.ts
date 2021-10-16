import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INotifications, getNotificationsIdentifier } from '../notifications.model';

export type EntityResponseType = HttpResponse<INotifications>;
export type EntityArrayResponseType = HttpResponse<INotifications[]>;

@Injectable({ providedIn: 'root' })
export class NotificationsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/notifications');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(notifications: INotifications): Observable<EntityResponseType> {
    return this.http.post<INotifications>(this.resourceUrl, notifications, { observe: 'response' });
  }

  update(notifications: INotifications): Observable<EntityResponseType> {
    return this.http.put<INotifications>(`${this.resourceUrl}/${getNotificationsIdentifier(notifications) as number}`, notifications, {
      observe: 'response',
    });
  }

  partialUpdate(notifications: INotifications): Observable<EntityResponseType> {
    return this.http.patch<INotifications>(`${this.resourceUrl}/${getNotificationsIdentifier(notifications) as number}`, notifications, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INotifications>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INotifications[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addNotificationsToCollectionIfMissing(
    notificationsCollection: INotifications[],
    ...notificationsToCheck: (INotifications | null | undefined)[]
  ): INotifications[] {
    const notifications: INotifications[] = notificationsToCheck.filter(isPresent);
    if (notifications.length > 0) {
      const notificationsCollectionIdentifiers = notificationsCollection.map(
        notificationsItem => getNotificationsIdentifier(notificationsItem)!
      );
      const notificationsToAdd = notifications.filter(notificationsItem => {
        const notificationsIdentifier = getNotificationsIdentifier(notificationsItem);
        if (notificationsIdentifier == null || notificationsCollectionIdentifiers.includes(notificationsIdentifier)) {
          return false;
        }
        notificationsCollectionIdentifiers.push(notificationsIdentifier);
        return true;
      });
      return [...notificationsToAdd, ...notificationsCollection];
    }
    return notificationsCollection;
  }
}
