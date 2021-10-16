import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAdressList } from '../adress-list.model';
import { AdressListService } from '../service/adress-list.service';
import { AdressListDeleteDialogComponent } from '../delete/adress-list-delete-dialog.component';

@Component({
  selector: 'jhi-adress-list',
  templateUrl: './adress-list.component.html',
})
export class AdressListComponent implements OnInit {
  adressLists?: IAdressList[];
  isLoading = false;

  constructor(protected adressListService: AdressListService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.adressListService.query().subscribe(
      (res: HttpResponse<IAdressList[]>) => {
        this.isLoading = false;
        this.adressLists = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAdressList): number {
    return item.id!;
  }

  delete(adressList: IAdressList): void {
    const modalRef = this.modalService.open(AdressListDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.adressList = adressList;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
