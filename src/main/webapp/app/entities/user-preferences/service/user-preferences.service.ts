import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserPreferences, getUserPreferencesIdentifier } from '../user-preferences.model';

export type EntityResponseType = HttpResponse<IUserPreferences>;
export type EntityArrayResponseType = HttpResponse<IUserPreferences[]>;

@Injectable({ providedIn: 'root' })
export class UserPreferencesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-preferences');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(userPreferences: IUserPreferences): Observable<EntityResponseType> {
    return this.http.post<IUserPreferences>(this.resourceUrl, userPreferences, { observe: 'response' });
  }

  update(userPreferences: IUserPreferences): Observable<EntityResponseType> {
    return this.http.put<IUserPreferences>(
      `${this.resourceUrl}/${getUserPreferencesIdentifier(userPreferences) as number}`,
      userPreferences,
      { observe: 'response' }
    );
  }

  partialUpdate(userPreferences: IUserPreferences): Observable<EntityResponseType> {
    return this.http.patch<IUserPreferences>(
      `${this.resourceUrl}/${getUserPreferencesIdentifier(userPreferences) as number}`,
      userPreferences,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserPreferences>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserPreferences[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUserPreferencesToCollectionIfMissing(
    userPreferencesCollection: IUserPreferences[],
    ...userPreferencesToCheck: (IUserPreferences | null | undefined)[]
  ): IUserPreferences[] {
    const userPreferences: IUserPreferences[] = userPreferencesToCheck.filter(isPresent);
    if (userPreferences.length > 0) {
      const userPreferencesCollectionIdentifiers = userPreferencesCollection.map(
        userPreferencesItem => getUserPreferencesIdentifier(userPreferencesItem)!
      );
      const userPreferencesToAdd = userPreferences.filter(userPreferencesItem => {
        const userPreferencesIdentifier = getUserPreferencesIdentifier(userPreferencesItem);
        if (userPreferencesIdentifier == null || userPreferencesCollectionIdentifiers.includes(userPreferencesIdentifier)) {
          return false;
        }
        userPreferencesCollectionIdentifiers.push(userPreferencesIdentifier);
        return true;
      });
      return [...userPreferencesToAdd, ...userPreferencesCollection];
    }
    return userPreferencesCollection;
  }
}
