import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITripdetail } from '../tripdetail.model';
import { TripdetailService } from '../service/tripdetail.service';
import { TripdetailDeleteDialogComponent } from '../delete/tripdetail-delete-dialog.component';

@Component({
  selector: 'jhi-tripdetail',
  templateUrl: './tripdetail.component.html',
})
export class TripdetailComponent implements OnInit {
  tripdetails?: ITripdetail[];
  isLoading = false;

  constructor(protected tripdetailService: TripdetailService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tripdetailService.query().subscribe(
      (res: HttpResponse<ITripdetail[]>) => {
        this.isLoading = false;
        this.tripdetails = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITripdetail): number {
    return item.id!;
  }

  delete(tripdetail: ITripdetail): void {
    const modalRef = this.modalService.open(TripdetailDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tripdetail = tripdetail;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
