import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAdressList } from '../adress-list.model';
import { AdressListService } from '../service/adress-list.service';

@Component({
  templateUrl: './adress-list-delete-dialog.component.html',
})
export class AdressListDeleteDialogComponent {
  adressList?: IAdressList;

  constructor(protected adressListService: AdressListService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.adressListService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
