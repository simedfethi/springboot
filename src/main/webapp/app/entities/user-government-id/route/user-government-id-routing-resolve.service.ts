import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserGovernmentID, UserGovernmentID } from '../user-government-id.model';
import { UserGovernmentIDService } from '../service/user-government-id.service';

@Injectable({ providedIn: 'root' })
export class UserGovernmentIDRoutingResolveService implements Resolve<IUserGovernmentID> {
  constructor(protected service: UserGovernmentIDService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserGovernmentID> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((userGovernmentID: HttpResponse<UserGovernmentID>) => {
          if (userGovernmentID.body) {
            return of(userGovernmentID.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UserGovernmentID());
  }
}
