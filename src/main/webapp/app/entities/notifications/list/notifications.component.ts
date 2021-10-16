import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { INotifications } from '../notifications.model';
import { NotificationsService } from '../service/notifications.service';
import { NotificationsDeleteDialogComponent } from '../delete/notifications-delete-dialog.component';

@Component({
  selector: 'jhi-notifications',
  templateUrl: './notifications.component.html',
})
export class NotificationsComponent implements OnInit {
  notifications?: INotifications[];
  isLoading = false;

  constructor(protected notificationsService: NotificationsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.notificationsService.query().subscribe(
      (res: HttpResponse<INotifications[]>) => {
        this.isLoading = false;
        this.notifications = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: INotifications): number {
    return item.id!;
  }

  delete(notifications: INotifications): void {
    const modalRef = this.modalService.open(NotificationsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.notifications = notifications;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
