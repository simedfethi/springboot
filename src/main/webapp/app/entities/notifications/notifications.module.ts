import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NotificationsComponent } from './list/notifications.component';
import { NotificationsDetailComponent } from './detail/notifications-detail.component';
import { NotificationsUpdateComponent } from './update/notifications-update.component';
import { NotificationsDeleteDialogComponent } from './delete/notifications-delete-dialog.component';
import { NotificationsRoutingModule } from './route/notifications-routing.module';

@NgModule({
  imports: [SharedModule, NotificationsRoutingModule],
  declarations: [NotificationsComponent, NotificationsDetailComponent, NotificationsUpdateComponent, NotificationsDeleteDialogComponent],
  entryComponents: [NotificationsDeleteDialogComponent],
})
export class NotificationsModule {}
