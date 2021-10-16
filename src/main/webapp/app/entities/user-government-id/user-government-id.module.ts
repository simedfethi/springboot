import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UserGovernmentIDComponent } from './list/user-government-id.component';
import { UserGovernmentIDDetailComponent } from './detail/user-government-id-detail.component';
import { UserGovernmentIDUpdateComponent } from './update/user-government-id-update.component';
import { UserGovernmentIDDeleteDialogComponent } from './delete/user-government-id-delete-dialog.component';
import { UserGovernmentIDRoutingModule } from './route/user-government-id-routing.module';

@NgModule({
  imports: [SharedModule, UserGovernmentIDRoutingModule],
  declarations: [
    UserGovernmentIDComponent,
    UserGovernmentIDDetailComponent,
    UserGovernmentIDUpdateComponent,
    UserGovernmentIDDeleteDialogComponent,
  ],
  entryComponents: [UserGovernmentIDDeleteDialogComponent],
})
export class UserGovernmentIDModule {}
