import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITripGroupMembers } from '../trip-group-members.model';
import { TripGroupMembersService } from '../service/trip-group-members.service';
import { TripGroupMembersDeleteDialogComponent } from '../delete/trip-group-members-delete-dialog.component';

@Component({
  selector: 'jhi-trip-group-members',
  templateUrl: './trip-group-members.component.html',
})
export class TripGroupMembersComponent implements OnInit {
  tripGroupMembers?: ITripGroupMembers[];
  isLoading = false;

  constructor(protected tripGroupMembersService: TripGroupMembersService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tripGroupMembersService.query().subscribe(
      (res: HttpResponse<ITripGroupMembers[]>) => {
        this.isLoading = false;
        this.tripGroupMembers = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITripGroupMembers): number {
    return item.id!;
  }

  delete(tripGroupMembers: ITripGroupMembers): void {
    const modalRef = this.modalService.open(TripGroupMembersDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tripGroupMembers = tripGroupMembers;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
