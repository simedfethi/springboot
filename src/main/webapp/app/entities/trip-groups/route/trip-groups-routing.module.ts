import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TripGroupsComponent } from '../list/trip-groups.component';
import { TripGroupsDetailComponent } from '../detail/trip-groups-detail.component';
import { TripGroupsUpdateComponent } from '../update/trip-groups-update.component';
import { TripGroupsRoutingResolveService } from './trip-groups-routing-resolve.service';

const tripGroupsRoute: Routes = [
  {
    path: '',
    component: TripGroupsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TripGroupsDetailComponent,
    resolve: {
      tripGroups: TripGroupsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TripGroupsUpdateComponent,
    resolve: {
      tripGroups: TripGroupsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TripGroupsUpdateComponent,
    resolve: {
      tripGroups: TripGroupsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tripGroupsRoute)],
  exports: [RouterModule],
})
export class TripGroupsRoutingModule {}
