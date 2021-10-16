import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserPhone, UserPhone } from '../user-phone.model';
import { UserPhoneService } from '../service/user-phone.service';

@Injectable({ providedIn: 'root' })
export class UserPhoneRoutingResolveService implements Resolve<IUserPhone> {
  constructor(protected service: UserPhoneService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserPhone> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((userPhone: HttpResponse<UserPhone>) => {
          if (userPhone.body) {
            return of(userPhone.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UserPhone());
  }
}
