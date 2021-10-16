import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TripdetailComponent } from '../list/tripdetail.component';
import { TripdetailDetailComponent } from '../detail/tripdetail-detail.component';
import { TripdetailUpdateComponent } from '../update/tripdetail-update.component';
import { TripdetailRoutingResolveService } from './tripdetail-routing-resolve.service';

const tripdetailRoute: Routes = [
  {
    path: '',
    component: TripdetailComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TripdetailDetailComponent,
    resolve: {
      tripdetail: TripdetailRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TripdetailUpdateComponent,
    resolve: {
      tripdetail: TripdetailRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TripdetailUpdateComponent,
    resolve: {
      tripdetail: TripdetailRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tripdetailRoute)],
  exports: [RouterModule],
})
export class TripdetailRoutingModule {}
