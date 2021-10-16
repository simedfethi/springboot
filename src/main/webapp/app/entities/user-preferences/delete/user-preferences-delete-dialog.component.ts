import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserPreferences } from '../user-preferences.model';
import { UserPreferencesService } from '../service/user-preferences.service';

@Component({
  templateUrl: './user-preferences-delete-dialog.component.html',
})
export class UserPreferencesDeleteDialogComponent {
  userPreferences?: IUserPreferences;

  constructor(protected userPreferencesService: UserPreferencesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userPreferencesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
