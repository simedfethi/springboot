import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITripGroups } from '../trip-groups.model';
import { TripGroupsService } from '../service/trip-groups.service';

@Component({
  templateUrl: './trip-groups-delete-dialog.component.html',
})
export class TripGroupsDeleteDialogComponent {
  tripGroups?: ITripGroups;

  constructor(protected tripGroupsService: TripGroupsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tripGroupsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
