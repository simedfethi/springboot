import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITripGroups } from '../trip-groups.model';

@Component({
  selector: 'jhi-trip-groups-detail',
  templateUrl: './trip-groups-detail.component.html',
})
export class TripGroupsDetailComponent implements OnInit {
  tripGroups: ITripGroups | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tripGroups }) => {
      this.tripGroups = tripGroups;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
