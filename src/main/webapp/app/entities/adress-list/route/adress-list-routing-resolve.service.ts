import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAdressList, AdressList } from '../adress-list.model';
import { AdressListService } from '../service/adress-list.service';

@Injectable({ providedIn: 'root' })
export class AdressListRoutingResolveService implements Resolve<IAdressList> {
  constructor(protected service: AdressListService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAdressList> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((adressList: HttpResponse<AdressList>) => {
          if (adressList.body) {
            return of(adressList.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AdressList());
  }
}
