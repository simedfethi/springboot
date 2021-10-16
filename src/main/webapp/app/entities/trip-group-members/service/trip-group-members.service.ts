import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITripGroupMembers, getTripGroupMembersIdentifier } from '../trip-group-members.model';

export type EntityResponseType = HttpResponse<ITripGroupMembers>;
export type EntityArrayResponseType = HttpResponse<ITripGroupMembers[]>;

@Injectable({ providedIn: 'root' })
export class TripGroupMembersService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trip-group-members');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tripGroupMembers: ITripGroupMembers): Observable<EntityResponseType> {
    return this.http.post<ITripGroupMembers>(this.resourceUrl, tripGroupMembers, { observe: 'response' });
  }

  update(tripGroupMembers: ITripGroupMembers): Observable<EntityResponseType> {
    return this.http.put<ITripGroupMembers>(
      `${this.resourceUrl}/${getTripGroupMembersIdentifier(tripGroupMembers) as number}`,
      tripGroupMembers,
      { observe: 'response' }
    );
  }

  partialUpdate(tripGroupMembers: ITripGroupMembers): Observable<EntityResponseType> {
    return this.http.patch<ITripGroupMembers>(
      `${this.resourceUrl}/${getTripGroupMembersIdentifier(tripGroupMembers) as number}`,
      tripGroupMembers,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITripGroupMembers>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITripGroupMembers[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTripGroupMembersToCollectionIfMissing(
    tripGroupMembersCollection: ITripGroupMembers[],
    ...tripGroupMembersToCheck: (ITripGroupMembers | null | undefined)[]
  ): ITripGroupMembers[] {
    const tripGroupMembers: ITripGroupMembers[] = tripGroupMembersToCheck.filter(isPresent);
    if (tripGroupMembers.length > 0) {
      const tripGroupMembersCollectionIdentifiers = tripGroupMembersCollection.map(
        tripGroupMembersItem => getTripGroupMembersIdentifier(tripGroupMembersItem)!
      );
      const tripGroupMembersToAdd = tripGroupMembers.filter(tripGroupMembersItem => {
        const tripGroupMembersIdentifier = getTripGroupMembersIdentifier(tripGroupMembersItem);
        if (tripGroupMembersIdentifier == null || tripGroupMembersCollectionIdentifiers.includes(tripGroupMembersIdentifier)) {
          return false;
        }
        tripGroupMembersCollectionIdentifiers.push(tripGroupMembersIdentifier);
        return true;
      });
      return [...tripGroupMembersToAdd, ...tripGroupMembersCollection];
    }
    return tripGroupMembersCollection;
  }
}
