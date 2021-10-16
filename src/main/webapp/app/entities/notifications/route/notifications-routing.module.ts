import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NotificationsComponent } from '../list/notifications.component';
import { NotificationsDetailComponent } from '../detail/notifications-detail.component';
import { NotificationsUpdateComponent } from '../update/notifications-update.component';
import { NotificationsRoutingResolveService } from './notifications-routing-resolve.service';

const notificationsRoute: Routes = [
  {
    path: '',
    component: NotificationsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NotificationsDetailComponent,
    resolve: {
      notifications: NotificationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NotificationsUpdateComponent,
    resolve: {
      notifications: NotificationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NotificationsUpdateComponent,
    resolve: {
      notifications: NotificationsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(notificationsRoute)],
  exports: [RouterModule],
})
export class NotificationsRoutingModule {}
