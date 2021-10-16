import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UserPhoneComponent } from './list/user-phone.component';
import { UserPhoneDetailComponent } from './detail/user-phone-detail.component';
import { UserPhoneUpdateComponent } from './update/user-phone-update.component';
import { UserPhoneDeleteDialogComponent } from './delete/user-phone-delete-dialog.component';
import { UserPhoneRoutingModule } from './route/user-phone-routing.module';

@NgModule({
  imports: [SharedModule, UserPhoneRoutingModule],
  declarations: [UserPhoneComponent, UserPhoneDetailComponent, UserPhoneUpdateComponent, UserPhoneDeleteDialogComponent],
  entryComponents: [UserPhoneDeleteDialogComponent],
})
export class UserPhoneModule {}
