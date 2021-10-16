import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITripCategory } from '../trip-category.model';
import { TripCategoryService } from '../service/trip-category.service';
import { TripCategoryDeleteDialogComponent } from '../delete/trip-category-delete-dialog.component';

@Component({
  selector: 'jhi-trip-category',
  templateUrl: './trip-category.component.html',
})
export class TripCategoryComponent implements OnInit {
  tripCategories?: ITripCategory[];
  isLoading = false;

  constructor(protected tripCategoryService: TripCategoryService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tripCategoryService.query().subscribe(
      (res: HttpResponse<ITripCategory[]>) => {
        this.isLoading = false;
        this.tripCategories = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITripCategory): number {
    return item.id!;
  }

  delete(tripCategory: ITripCategory): void {
    const modalRef = this.modalService.open(TripCategoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tripCategory = tripCategory;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
