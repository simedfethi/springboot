import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserPhoneComponent } from '../list/user-phone.component';
import { UserPhoneDetailComponent } from '../detail/user-phone-detail.component';
import { UserPhoneUpdateComponent } from '../update/user-phone-update.component';
import { UserPhoneRoutingResolveService } from './user-phone-routing-resolve.service';

const userPhoneRoute: Routes = [
  {
    path: '',
    component: UserPhoneComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserPhoneDetailComponent,
    resolve: {
      userPhone: UserPhoneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserPhoneUpdateComponent,
    resolve: {
      userPhone: UserPhoneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserPhoneUpdateComponent,
    resolve: {
      userPhone: UserPhoneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(userPhoneRoute)],
  exports: [RouterModule],
})
export class UserPhoneRoutingModule {}
