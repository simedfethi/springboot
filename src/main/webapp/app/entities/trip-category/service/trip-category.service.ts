import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITripCategory, getTripCategoryIdentifier } from '../trip-category.model';

export type EntityResponseType = HttpResponse<ITripCategory>;
export type EntityArrayResponseType = HttpResponse<ITripCategory[]>;

@Injectable({ providedIn: 'root' })
export class TripCategoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trip-categories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tripCategory: ITripCategory): Observable<EntityResponseType> {
    return this.http.post<ITripCategory>(this.resourceUrl, tripCategory, { observe: 'response' });
  }

  update(tripCategory: ITripCategory): Observable<EntityResponseType> {
    return this.http.put<ITripCategory>(`${this.resourceUrl}/${getTripCategoryIdentifier(tripCategory) as number}`, tripCategory, {
      observe: 'response',
    });
  }

  partialUpdate(tripCategory: ITripCategory): Observable<EntityResponseType> {
    return this.http.patch<ITripCategory>(`${this.resourceUrl}/${getTripCategoryIdentifier(tripCategory) as number}`, tripCategory, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITripCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITripCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTripCategoryToCollectionIfMissing(
    tripCategoryCollection: ITripCategory[],
    ...tripCategoriesToCheck: (ITripCategory | null | undefined)[]
  ): ITripCategory[] {
    const tripCategories: ITripCategory[] = tripCategoriesToCheck.filter(isPresent);
    if (tripCategories.length > 0) {
      const tripCategoryCollectionIdentifiers = tripCategoryCollection.map(
        tripCategoryItem => getTripCategoryIdentifier(tripCategoryItem)!
      );
      const tripCategoriesToAdd = tripCategories.filter(tripCategoryItem => {
        const tripCategoryIdentifier = getTripCategoryIdentifier(tripCategoryItem);
        if (tripCategoryIdentifier == null || tripCategoryCollectionIdentifiers.includes(tripCategoryIdentifier)) {
          return false;
        }
        tripCategoryCollectionIdentifiers.push(tripCategoryIdentifier);
        return true;
      });
      return [...tripCategoriesToAdd, ...tripCategoryCollection];
    }
    return tripCategoryCollection;
  }
}
