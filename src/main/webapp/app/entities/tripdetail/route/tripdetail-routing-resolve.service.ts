import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITripdetail, Tripdetail } from '../tripdetail.model';
import { TripdetailService } from '../service/tripdetail.service';

@Injectable({ providedIn: 'root' })
export class TripdetailRoutingResolveService implements Resolve<ITripdetail> {
  constructor(protected service: TripdetailService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITripdetail> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tripdetail: HttpResponse<Tripdetail>) => {
          if (tripdetail.body) {
            return of(tripdetail.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tripdetail());
  }
}
