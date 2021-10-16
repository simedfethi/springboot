import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITripCategory, TripCategory } from '../trip-category.model';
import { TripCategoryService } from '../service/trip-category.service';

@Injectable({ providedIn: 'root' })
export class TripCategoryRoutingResolveService implements Resolve<ITripCategory> {
  constructor(protected service: TripCategoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITripCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tripCategory: HttpResponse<TripCategory>) => {
          if (tripCategory.body) {
            return of(tripCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TripCategory());
  }
}
