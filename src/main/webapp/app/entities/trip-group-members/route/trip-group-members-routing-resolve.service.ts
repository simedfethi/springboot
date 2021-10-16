import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITripGroupMembers, TripGroupMembers } from '../trip-group-members.model';
import { TripGroupMembersService } from '../service/trip-group-members.service';

@Injectable({ providedIn: 'root' })
export class TripGroupMembersRoutingResolveService implements Resolve<ITripGroupMembers> {
  constructor(protected service: TripGroupMembersService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITripGroupMembers> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tripGroupMembers: HttpResponse<TripGroupMembers>) => {
          if (tripGroupMembers.body) {
            return of(tripGroupMembers.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TripGroupMembers());
  }
}
