import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUserPreferences, UserPreferences } from '../user-preferences.model';
import { UserPreferencesService } from '../service/user-preferences.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-user-preferences-update',
  templateUrl: './user-preferences-update.component.html',
})
export class UserPreferencesUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    language: [null, [Validators.required, Validators.maxLength(50)]],
    currency: [null, [Validators.required, Validators.maxLength(50)]],
    timeZone: [null, [Validators.maxLength(50)]],
    internalUser: [],
  });

  constructor(
    protected userPreferencesService: UserPreferencesService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userPreferences }) => {
      this.updateForm(userPreferences);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userPreferences = this.createFromForm();
    if (userPreferences.id !== undefined) {
      this.subscribeToSaveResponse(this.userPreferencesService.update(userPreferences));
    } else {
      this.subscribeToSaveResponse(this.userPreferencesService.create(userPreferences));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserPreferences>>): void {
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

  protected updateForm(userPreferences: IUserPreferences): void {
    this.editForm.patchValue({
      id: userPreferences.id,
      language: userPreferences.language,
      currency: userPreferences.currency,
      timeZone: userPreferences.timeZone,
      internalUser: userPreferences.internalUser,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, userPreferences.internalUser);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('internalUser')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IUserPreferences {
    return {
      ...new UserPreferences(),
      id: this.editForm.get(['id'])!.value,
      language: this.editForm.get(['language'])!.value,
      currency: this.editForm.get(['currency'])!.value,
      timeZone: this.editForm.get(['timeZone'])!.value,
      internalUser: this.editForm.get(['internalUser'])!.value,
    };
  }
}
