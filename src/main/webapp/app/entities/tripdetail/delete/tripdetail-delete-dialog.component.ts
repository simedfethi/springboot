import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITripdetail } from '../tripdetail.model';
import { TripdetailService } from '../service/tripdetail.service';

@Component({
  templateUrl: './tripdetail-delete-dialog.component.html',
})
export class TripdetailDeleteDialogComponent {
  tripdetail?: ITripdetail;

  constructor(protected tripdetailService: TripdetailService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tripdetailService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
