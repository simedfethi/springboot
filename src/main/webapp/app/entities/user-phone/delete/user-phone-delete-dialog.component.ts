import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserPhone } from '../user-phone.model';
import { UserPhoneService } from '../service/user-phone.service';

@Component({
  templateUrl: './user-phone-delete-dialog.component.html',
})
export class UserPhoneDeleteDialogComponent {
  userPhone?: IUserPhone;

  constructor(protected userPhoneService: UserPhoneService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userPhoneService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
