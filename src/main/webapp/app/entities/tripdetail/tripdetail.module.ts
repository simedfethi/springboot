import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TripdetailComponent } from './list/tripdetail.component';
import { TripdetailDetailComponent } from './detail/tripdetail-detail.component';
import { TripdetailUpdateComponent } from './update/tripdetail-update.component';
import { TripdetailDeleteDialogComponent } from './delete/tripdetail-delete-dialog.component';
import { TripdetailRoutingModule } from './route/tripdetail-routing.module';

@NgModule({
  imports: [SharedModule, TripdetailRoutingModule],
  declarations: [TripdetailComponent, TripdetailDetailComponent, TripdetailUpdateComponent, TripdetailDeleteDialogComponent],
  entryComponents: [TripdetailDeleteDialogComponent],
})
export class TripdetailModule {}
