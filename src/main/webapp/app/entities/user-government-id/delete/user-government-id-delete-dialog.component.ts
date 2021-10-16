import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserGovernmentID } from '../user-government-id.model';
import { UserGovernmentIDService } from '../service/user-government-id.service';

@Component({
  templateUrl: './user-government-id-delete-dialog.component.html',
})
export class UserGovernmentIDDeleteDialogComponent {
  userGovernmentID?: IUserGovernmentID;

  constructor(protected userGovernmentIDService: UserGovernmentIDService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userGovernmentIDService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
