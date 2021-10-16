import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITripGroups } from '../trip-groups.model';
import { TripGroupsService } from '../service/trip-groups.service';
import { TripGroupsDeleteDialogComponent } from '../delete/trip-groups-delete-dialog.component';

@Component({
  selector: 'jhi-trip-groups',
  templateUrl: './trip-groups.component.html',
})
export class TripGroupsComponent implements OnInit {
  tripGroups?: ITripGroups[];
  isLoading = false;

  constructor(protected tripGroupsService: TripGroupsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tripGroupsService.query().subscribe(
      (res: HttpResponse<ITripGroups[]>) => {
        this.isLoading = false;
        this.tripGroups = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITripGroups): number {
    return item.id!;
  }

  delete(tripGroups: ITripGroups): void {
    const modalRef = this.modalService.open(TripGroupsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tripGroups = tripGroups;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
