import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITripGroups, TripGroups } from '../trip-groups.model';
import { TripGroupsService } from '../service/trip-groups.service';

@Injectable({ providedIn: 'root' })
export class TripGroupsRoutingResolveService implements Resolve<ITripGroups> {
  constructor(protected service: TripGroupsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITripGroups> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tripGroups: HttpResponse<TripGroups>) => {
          if (tripGroups.body) {
            return of(tripGroups.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TripGroups());
  }
}
