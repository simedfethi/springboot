import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITripGroupMembers } from '../trip-group-members.model';
import { TripGroupMembersService } from '../service/trip-group-members.service';

@Component({
  templateUrl: './trip-group-members-delete-dialog.component.html',
})
export class TripGroupMembersDeleteDialogComponent {
  tripGroupMembers?: ITripGroupMembers;

  constructor(protected tripGroupMembersService: TripGroupMembersService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tripGroupMembersService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
