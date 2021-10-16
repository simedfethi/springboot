import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserGovernmentID } from '../user-government-id.model';
import { UserGovernmentIDService } from '../service/user-government-id.service';
import { UserGovernmentIDDeleteDialogComponent } from '../delete/user-government-id-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-user-government-id',
  templateUrl: './user-government-id.component.html',
})
export class UserGovernmentIDComponent implements OnInit {
  userGovernmentIDS?: IUserGovernmentID[];
  isLoading = false;

  constructor(
    protected userGovernmentIDService: UserGovernmentIDService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.userGovernmentIDService.query().subscribe(
      (res: HttpResponse<IUserGovernmentID[]>) => {
        this.isLoading = false;
        this.userGovernmentIDS = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IUserGovernmentID): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(userGovernmentID: IUserGovernmentID): void {
    const modalRef = this.modalService.open(UserGovernmentIDDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.userGovernmentID = userGovernmentID;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
