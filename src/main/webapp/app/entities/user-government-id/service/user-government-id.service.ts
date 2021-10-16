import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserGovernmentID, getUserGovernmentIDIdentifier } from '../user-government-id.model';

export type EntityResponseType = HttpResponse<IUserGovernmentID>;
export type EntityArrayResponseType = HttpResponse<IUserGovernmentID[]>;

@Injectable({ providedIn: 'root' })
export class UserGovernmentIDService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-government-ids');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(userGovernmentID: IUserGovernmentID): Observable<EntityResponseType> {
    return this.http.post<IUserGovernmentID>(this.resourceUrl, userGovernmentID, { observe: 'response' });
  }

  update(userGovernmentID: IUserGovernmentID): Observable<EntityResponseType> {
    return this.http.put<IUserGovernmentID>(
      `${this.resourceUrl}/${getUserGovernmentIDIdentifier(userGovernmentID) as number}`,
      userGovernmentID,
      { observe: 'response' }
    );
  }

  partialUpdate(userGovernmentID: IUserGovernmentID): Observable<EntityResponseType> {
    return this.http.patch<IUserGovernmentID>(
      `${this.resourceUrl}/${getUserGovernmentIDIdentifier(userGovernmentID) as number}`,
      userGovernmentID,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserGovernmentID>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserGovernmentID[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUserGovernmentIDToCollectionIfMissing(
    userGovernmentIDCollection: IUserGovernmentID[],
    ...userGovernmentIDSToCheck: (IUserGovernmentID | null | undefined)[]
  ): IUserGovernmentID[] {
    const userGovernmentIDS: IUserGovernmentID[] = userGovernmentIDSToCheck.filter(isPresent);
    if (userGovernmentIDS.length > 0) {
      const userGovernmentIDCollectionIdentifiers = userGovernmentIDCollection.map(
        userGovernmentIDItem => getUserGovernmentIDIdentifier(userGovernmentIDItem)!
      );
      const userGovernmentIDSToAdd = userGovernmentIDS.filter(userGovernmentIDItem => {
        const userGovernmentIDIdentifier = getUserGovernmentIDIdentifier(userGovernmentIDItem);
        if (userGovernmentIDIdentifier == null || userGovernmentIDCollectionIdentifiers.includes(userGovernmentIDIdentifier)) {
          return false;
        }
        userGovernmentIDCollectionIdentifiers.push(userGovernmentIDIdentifier);
        return true;
      });
      return [...userGovernmentIDSToAdd, ...userGovernmentIDCollection];
    }
    return userGovernmentIDCollection;
  }
}
