import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AdressListComponent } from '../list/adress-list.component';
import { AdressListDetailComponent } from '../detail/adress-list-detail.component';
import { AdressListUpdateComponent } from '../update/adress-list-update.component';
import { AdressListRoutingResolveService } from './adress-list-routing-resolve.service';

const adressListRoute: Routes = [
  {
    path: '',
    component: AdressListComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AdressListDetailComponent,
    resolve: {
      adressList: AdressListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AdressListUpdateComponent,
    resolve: {
      adressList: AdressListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AdressListUpdateComponent,
    resolve: {
      adressList: AdressListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(adressListRoute)],
  exports: [RouterModule],
})
export class AdressListRoutingModule {}
