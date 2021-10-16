import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserGovernmentIDComponent } from '../list/user-government-id.component';
import { UserGovernmentIDDetailComponent } from '../detail/user-government-id-detail.component';
import { UserGovernmentIDUpdateComponent } from '../update/user-government-id-update.component';
import { UserGovernmentIDRoutingResolveService } from './user-government-id-routing-resolve.service';

const userGovernmentIDRoute: Routes = [
  {
    path: '',
    component: UserGovernmentIDComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserGovernmentIDDetailComponent,
    resolve: {
      userGovernmentID: UserGovernmentIDRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserGovernmentIDUpdateComponent,
    resolve: {
      userGovernmentID: UserGovernmentIDRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserGovernmentIDUpdateComponent,
    resolve: {
      userGovernmentID: UserGovernmentIDRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(userGovernmentIDRoute)],
  exports: [RouterModule],
})
export class UserGovernmentIDRoutingModule {}
