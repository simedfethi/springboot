import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITripdetail, getTripdetailIdentifier } from '../tripdetail.model';

export type EntityResponseType = HttpResponse<ITripdetail>;
export type EntityArrayResponseType = HttpResponse<ITripdetail[]>;

@Injectable({ providedIn: 'root' })
export class TripdetailService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tripdetails');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tripdetail: ITripdetail): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tripdetail);
    return this.http
      .post<ITripdetail>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tripdetail: ITripdetail): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tripdetail);
    return this.http
      .put<ITripdetail>(`${this.resourceUrl}/${getTripdetailIdentifier(tripdetail) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tripdetail: ITripdetail): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tripdetail);
    return this.http
      .patch<ITripdetail>(`${this.resourceUrl}/${getTripdetailIdentifier(tripdetail) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITripdetail>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITripdetail[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTripdetailToCollectionIfMissing(
    tripdetailCollection: ITripdetail[],
    ...tripdetailsToCheck: (ITripdetail | null | undefined)[]
  ): ITripdetail[] {
    const tripdetails: ITripdetail[] = tripdetailsToCheck.filter(isPresent);
    if (tripdetails.length > 0) {
      const tripdetailCollectionIdentifiers = tripdetailCollection.map(tripdetailItem => getTripdetailIdentifier(tripdetailItem)!);
      const tripdetailsToAdd = tripdetails.filter(tripdetailItem => {
        const tripdetailIdentifier = getTripdetailIdentifier(tripdetailItem);
        if (tripdetailIdentifier == null || tripdetailCollectionIdentifiers.includes(tripdetailIdentifier)) {
          return false;
        }
        tripdetailCollectionIdentifiers.push(tripdetailIdentifier);
        return true;
      });
      return [...tripdetailsToAdd, ...tripdetailCollection];
    }
    return tripdetailCollection;
  }

  protected convertDateFromClient(tripdetail: ITripdetail): ITripdetail {
    return Object.assign({}, tripdetail, {
      createdDate: tripdetail.createdDate?.isValid() ? tripdetail.createdDate.format(DATE_FORMAT) : undefined,
      lastupdated: tripdetail.lastupdated?.isValid() ? tripdetail.lastupdated.format(DATE_FORMAT) : undefined,
      departureDate: tripdetail.departureDate?.isValid() ? tripdetail.departureDate.format(DATE_FORMAT) : undefined,
      arrivalDate: tripdetail.arrivalDate?.isValid() ? tripdetail.arrivalDate.format(DATE_FORMAT) : undefined,
      contentDate: tripdetail.contentDate?.isValid() ? tripdetail.contentDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? dayjs(res.body.createdDate) : undefined;
      res.body.lastupdated = res.body.lastupdated ? dayjs(res.body.lastupdated) : undefined;
      res.body.departureDate = res.body.departureDate ? dayjs(res.body.departureDate) : undefined;
      res.body.arrivalDate = res.body.arrivalDate ? dayjs(res.body.arrivalDate) : undefined;
      res.body.contentDate = res.body.contentDate ? dayjs(res.body.contentDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((tripdetail: ITripdetail) => {
        tripdetail.createdDate = tripdetail.createdDate ? dayjs(tripdetail.createdDate) : undefined;
        tripdetail.lastupdated = tripdetail.lastupdated ? dayjs(tripdetail.lastupdated) : undefined;
        tripdetail.departureDate = tripdetail.departureDate ? dayjs(tripdetail.departureDate) : undefined;
        tripdetail.arrivalDate = tripdetail.arrivalDate ? dayjs(tripdetail.arrivalDate) : undefined;
        tripdetail.contentDate = tripdetail.contentDate ? dayjs(tripdetail.contentDate) : undefined;
      });
    }
    return res;
  }
}
