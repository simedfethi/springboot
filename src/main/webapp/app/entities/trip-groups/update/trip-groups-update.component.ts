import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITripGroups, TripGroups } from '../trip-groups.model';
import { TripGroupsService } from '../service/trip-groups.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-trip-groups-update',
  templateUrl: './trip-groups-update.component.html',
})
export class TripGroupsUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.maxLength(50)]],
    description: [null, [Validators.maxLength(50)]],
    internalUsers: [],
  });

  constructor(
    protected tripGroupsService: TripGroupsService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tripGroups }) => {
      this.updateForm(tripGroups);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tripGroups = this.createFromForm();
    if (tripGroups.id !== undefined) {
      this.subscribeToSaveResponse(this.tripGroupsService.update(tripGroups));
    } else {
      this.subscribeToSaveResponse(this.tripGroupsService.create(tripGroups));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  getSelectedUser(option: IUser, selectedVals?: IUser[]): IUser {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITripGroups>>): void {
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

  protected updateForm(tripGroups: ITripGroups): void {
    this.editForm.patchValue({
      id: tripGroups.id,
      title: tripGroups.title,
      description: tripGroups.description,
      internalUsers: tripGroups.internalUsers,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      ...(tripGroups.internalUsers ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, ...(this.editForm.get('internalUsers')!.value ?? [])))
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): ITripGroups {
    return {
      ...new TripGroups(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      internalUsers: this.editForm.get(['internalUsers'])!.value,
    };
  }
}
