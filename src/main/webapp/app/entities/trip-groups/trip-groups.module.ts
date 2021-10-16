import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TripGroupsComponent } from './list/trip-groups.component';
import { TripGroupsDetailComponent } from './detail/trip-groups-detail.component';
import { TripGroupsUpdateComponent } from './update/trip-groups-update.component';
import { TripGroupsDeleteDialogComponent } from './delete/trip-groups-delete-dialog.component';
import { TripGroupsRoutingModule } from './route/trip-groups-routing.module';

@NgModule({
  imports: [SharedModule, TripGroupsRoutingModule],
  declarations: [TripGroupsComponent, TripGroupsDetailComponent, TripGroupsUpdateComponent, TripGroupsDeleteDialogComponent],
  entryComponents: [TripGroupsDeleteDialogComponent],
})
export class TripGroupsModule {}
