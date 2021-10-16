import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITripCategory } from '../trip-category.model';

@Component({
  selector: 'jhi-trip-category-detail',
  templateUrl: './trip-category-detail.component.html',
})
export class TripCategoryDetailComponent implements OnInit {
  tripCategory: ITripCategory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tripCategory }) => {
      this.tripCategory = tripCategory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
