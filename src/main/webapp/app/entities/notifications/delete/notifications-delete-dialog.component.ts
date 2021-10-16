import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INotifications } from '../notifications.model';
import { NotificationsService } from '../service/notifications.service';

@Component({
  templateUrl: './notifications-delete-dialog.component.html',
})
export class NotificationsDeleteDialogComponent {
  notifications?: INotifications;

  constructor(protected notificationsService: NotificationsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.notificationsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
