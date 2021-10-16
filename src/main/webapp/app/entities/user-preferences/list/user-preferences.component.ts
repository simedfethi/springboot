import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserPreferences } from '../user-preferences.model';
import { UserPreferencesService } from '../service/user-preferences.service';
import { UserPreferencesDeleteDialogComponent } from '../delete/user-preferences-delete-dialog.component';

@Component({
  selector: 'jhi-user-preferences',
  templateUrl: './user-preferences.component.html',
})
export class UserPreferencesComponent implements OnInit {
  userPreferences?: IUserPreferences[];
  isLoading = false;

  constructor(protected userPreferencesService: UserPreferencesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.userPreferencesService.query().subscribe(
      (res: HttpResponse<IUserPreferences[]>) => {
        this.isLoading = false;
        this.userPreferences = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IUserPreferences): number {
    return item.id!;
  }

  delete(userPreferences: IUserPreferences): void {
    const modalRef = this.modalService.open(UserPreferencesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.userPreferences = userPreferences;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
