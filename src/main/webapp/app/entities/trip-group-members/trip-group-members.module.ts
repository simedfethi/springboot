import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TripGroupMembersComponent } from './list/trip-group-members.component';
import { TripGroupMembersDetailComponent } from './detail/trip-group-members-detail.component';
import { TripGroupMembersUpdateComponent } from './update/trip-group-members-update.component';
import { TripGroupMembersDeleteDialogComponent } from './delete/trip-group-members-delete-dialog.component';
import { TripGroupMembersRoutingModule } from './route/trip-group-members-routing.module';

@NgModule({
  imports: [SharedModule, TripGroupMembersRoutingModule],
  declarations: [
    TripGroupMembersComponent,
    TripGroupMembersDetailComponent,
    TripGroupMembersUpdateComponent,
    TripGroupMembersDeleteDialogComponent,
  ],
  entryComponents: [TripGroupMembersDeleteDialogComponent],
})
export class TripGroupMembersModule {}
