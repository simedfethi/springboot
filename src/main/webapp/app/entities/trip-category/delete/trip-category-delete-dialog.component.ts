import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITripCategory } from '../trip-category.model';
import { TripCategoryService } from '../service/trip-category.service';

@Component({
  templateUrl: './trip-category-delete-dialog.component.html',
})
export class TripCategoryDeleteDialogComponent {
  tripCategory?: ITripCategory;

  constructor(protected tripCategoryService: TripCategoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tripCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
