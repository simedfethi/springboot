import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITripGroupMembers } from '../trip-group-members.model';

@Component({
  selector: 'jhi-trip-group-members-detail',
  templateUrl: './trip-group-members-detail.component.html',
})
export class TripGroupMembersDetailComponent implements OnInit {
  tripGroupMembers: ITripGroupMembers | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tripGroupMembers }) => {
      this.tripGroupMembers = tripGroupMembers;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
