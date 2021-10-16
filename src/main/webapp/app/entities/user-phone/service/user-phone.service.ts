import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserPhone, getUserPhoneIdentifier } from '../user-phone.model';

export type EntityResponseType = HttpResponse<IUserPhone>;
export type EntityArrayResponseType = HttpResponse<IUserPhone[]>;

@Injectable({ providedIn: 'root' })
export class UserPhoneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-phones');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(userPhone: IUserPhone): Observable<EntityResponseType> {
    return this.http.post<IUserPhone>(this.resourceUrl, userPhone, { observe: 'response' });
  }

  update(userPhone: IUserPhone): Observable<EntityResponseType> {
    return this.http.put<IUserPhone>(`${this.resourceUrl}/${getUserPhoneIdentifier(userPhone) as number}`, userPhone, {
      observe: 'response',
    });
  }

  partialUpdate(userPhone: IUserPhone): Observable<EntityResponseType> {
    return this.http.patch<IUserPhone>(`${this.resourceUrl}/${getUserPhoneIdentifier(userPhone) as number}`, userPhone, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserPhone>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserPhone[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUserPhoneToCollectionIfMissing(
    userPhoneCollection: IUserPhone[],
    ...userPhonesToCheck: (IUserPhone | null | undefined)[]
  ): IUserPhone[] {
    const userPhones: IUserPhone[] = userPhonesToCheck.filter(isPresent);
    if (userPhones.length > 0) {
      const userPhoneCollectionIdentifiers = userPhoneCollection.map(userPhoneItem => getUserPhoneIdentifier(userPhoneItem)!);
      const userPhonesToAdd = userPhones.filter(userPhoneItem => {
        const userPhoneIdentifier = getUserPhoneIdentifier(userPhoneItem);
        if (userPhoneIdentifier == null || userPhoneCollectionIdentifiers.includes(userPhoneIdentifier)) {
          return false;
        }
        userPhoneCollectionIdentifiers.push(userPhoneIdentifier);
        return true;
      });
      return [...userPhonesToAdd, ...userPhoneCollection];
    }
    return userPhoneCollection;
  }
}
