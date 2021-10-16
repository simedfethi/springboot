import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITripGroupMembers, TripGroupMembers } from '../trip-group-members.model';
import { TripGroupMembersService } from '../service/trip-group-members.service';

@Component({
  selector: 'jhi-trip-group-members-update',
  templateUrl: './trip-group-members-update.component.html',
})
export class TripGroupMembersUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(
    protected tripGroupMembersService: TripGroupMembersService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tripGroupMembers }) => {
      this.updateForm(tripGroupMembers);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tripGroupMembers = this.createFromForm();
    if (tripGroupMembers.id !== undefined) {
      this.subscribeToSaveResponse(this.tripGroupMembersService.update(tripGroupMembers));
    } else {
      this.subscribeToSaveResponse(this.tripGroupMembersService.create(tripGroupMembers));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITripGroupMembers>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(tripGroupMembers: ITripGroupMembers): void {
    this.editForm.patchValue({
      id: tripGroupMembers.id,
    });
  }

  protected createFromForm(): ITripGroupMembers {
    return {
      ...new TripGroupMembers(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
