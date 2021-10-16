import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TripCategoryComponent } from '../list/trip-category.component';
import { TripCategoryDetailComponent } from '../detail/trip-category-detail.component';
import { TripCategoryUpdateComponent } from '../update/trip-category-update.component';
import { TripCategoryRoutingResolveService } from './trip-category-routing-resolve.service';

const tripCategoryRoute: Routes = [
  {
    path: '',
    component: TripCategoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TripCategoryDetailComponent,
    resolve: {
      tripCategory: TripCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TripCategoryUpdateComponent,
    resolve: {
      tripCategory: TripCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TripCategoryUpdateComponent,
    resolve: {
      tripCategory: TripCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tripCategoryRoute)],
  exports: [RouterModule],
})
export class TripCategoryRoutingModule {}
