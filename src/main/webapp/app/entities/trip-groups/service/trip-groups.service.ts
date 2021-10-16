import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITripGroups, getTripGroupsIdentifier } from '../trip-groups.model';

export type EntityResponseType = HttpResponse<ITripGroups>;
export type EntityArrayResponseType = HttpResponse<ITripGroups[]>;

@Injectable({ providedIn: 'root' })
export class TripGroupsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trip-groups');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tripGroups: ITripGroups): Observable<EntityResponseType> {
    return this.http.post<ITripGroups>(this.resourceUrl, tripGroups, { observe: 'response' });
  }

  update(tripGroups: ITripGroups): Observable<EntityResponseType> {
    return this.http.put<ITripGroups>(`${this.resourceUrl}/${getTripGroupsIdentifier(tripGroups) as number}`, tripGroups, {
      observe: 'response',
    });
  }

  partialUpdate(tripGroups: ITripGroups): Observable<EntityResponseType> {
    return this.http.patch<ITripGroups>(`${this.resourceUrl}/${getTripGroupsIdentifier(tripGroups) as number}`, tripGroups, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITripGroups>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITripGroups[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTripGroupsToCollectionIfMissing(
    tripGroupsCollection: ITripGroups[],
    ...tripGroupsToCheck: (ITripGroups | null | undefined)[]
  ): ITripGroups[] {
    const tripGroups: ITripGroups[] = tripGroupsToCheck.filter(isPresent);
    if (tripGroups.length > 0) {
      const tripGroupsCollectionIdentifiers = tripGroupsCollection.map(tripGroupsItem => getTripGroupsIdentifier(tripGroupsItem)!);
      const tripGroupsToAdd = tripGroups.filter(tripGroupsItem => {
        const tripGroupsIdentifier = getTripGroupsIdentifier(tripGroupsItem);
        if (tripGroupsIdentifier == null || tripGroupsCollectionIdentifiers.includes(tripGroupsIdentifier)) {
          return false;
        }
        tripGroupsCollectionIdentifiers.push(tripGroupsIdentifier);
        return true;
      });
      return [...tripGroupsToAdd, ...tripGroupsCollection];
    }
    return tripGroupsCollection;
  }
}
