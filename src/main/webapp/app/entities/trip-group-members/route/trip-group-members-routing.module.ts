import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TripGroupMembersComponent } from '../list/trip-group-members.component';
import { TripGroupMembersDetailComponent } from '../detail/trip-group-members-detail.component';
import { TripGroupMembersUpdateComponent } from '../update/trip-group-members-update.component';
import { TripGroupMembersRoutingResolveService } from './trip-group-members-routing-resolve.service';

const tripGroupMembersRoute: Routes = [
  {
    path: '',
    component: TripGroupMembersComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TripGroupMembersDetailComponent,
    resolve: {
      tripGroupMembers: TripGroupMembersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TripGroupMembersUpdateComponent,
    resolve: {
      tripGroupMembers: TripGroupMembersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TripGroupMembersUpdateComponent,
    resolve: {
      tripGroupMembers: TripGroupMembersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tripGroupMembersRoute)],
  exports: [RouterModule],
})
export class TripGroupMembersRoutingModule {}
