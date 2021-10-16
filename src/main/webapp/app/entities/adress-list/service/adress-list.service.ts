import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAdressList, getAdressListIdentifier } from '../adress-list.model';

export type EntityResponseType = HttpResponse<IAdressList>;
export type EntityArrayResponseType = HttpResponse<IAdressList[]>;

@Injectable({ providedIn: 'root' })
export class AdressListService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/adress-lists');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(adressList: IAdressList): Observable<EntityResponseType> {
    return this.http.post<IAdressList>(this.resourceUrl, adressList, { observe: 'response' });
  }

  update(adressList: IAdressList): Observable<EntityResponseType> {
    return this.http.put<IAdressList>(`${this.resourceUrl}/${getAdressListIdentifier(adressList) as number}`, adressList, {
      observe: 'response',
    });
  }

  partialUpdate(adressList: IAdressList): Observable<EntityResponseType> {
    return this.http.patch<IAdressList>(`${this.resourceUrl}/${getAdressListIdentifier(adressList) as number}`, adressList, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAdressList>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAdressList[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAdressListToCollectionIfMissing(
    adressListCollection: IAdressList[],
    ...adressListsToCheck: (IAdressList | null | undefined)[]
  ): IAdressList[] {
    const adressLists: IAdressList[] = adressListsToCheck.filter(isPresent);
    if (adressLists.length > 0) {
      const adressListCollectionIdentifiers = adressListCollection.map(adressListItem => getAdressListIdentifier(adressListItem)!);
      const adressListsToAdd = adressLists.filter(adressListItem => {
        const adressListIdentifier = getAdressListIdentifier(adressListItem);
        if (adressListIdentifier == null || adressListCollectionIdentifiers.includes(adressListIdentifier)) {
          return false;
        }
        adressListCollectionIdentifiers.push(adressListIdentifier);
        return true;
      });
      return [...adressListsToAdd, ...adressListCollection];
    }
    return adressListCollection;
  }
}
