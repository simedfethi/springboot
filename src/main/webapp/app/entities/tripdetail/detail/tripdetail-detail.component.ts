import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITripdetail } from '../tripdetail.model';

@Component({
  selector: 'jhi-tripdetail-detail',
  templateUrl: './tripdetail-detail.component.html',
})
export class TripdetailDetailComponent implements OnInit {
  tripdetail: ITripdetail | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tripdetail }) => {
      this.tripdetail = tripdetail;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
