import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserPhone } from '../user-phone.model';
import { UserPhoneService } from '../service/user-phone.service';
import { UserPhoneDeleteDialogComponent } from '../delete/user-phone-delete-dialog.component';

@Component({
  selector: 'jhi-user-phone',
  templateUrl: './user-phone.component.html',
})
export class UserPhoneComponent implements OnInit {
  userPhones?: IUserPhone[];
  isLoading = false;

  constructor(protected userPhoneService: UserPhoneService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.userPhoneService.query().subscribe(
      (res: HttpResponse<IUserPhone[]>) => {
        this.isLoading = false;
        this.userPhones = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IUserPhone): number {
    return item.id!;
  }

  delete(userPhone: IUserPhone): void {
    const modalRef = this.modalService.open(UserPhoneDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.userPhone = userPhone;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
