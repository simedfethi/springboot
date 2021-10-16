import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUserPhone, UserPhone } from '../user-phone.model';
import { UserPhoneService } from '../service/user-phone.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-user-phone-update',
  templateUrl: './user-phone-update.component.html',
})
export class UserPhoneUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    phoneNumber: [null, [Validators.required, Validators.maxLength(50)]],
    verified: [],
    internalUser: [],
  });

  constructor(
    protected userPhoneService: UserPhoneService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userPhone }) => {
      this.updateForm(userPhone);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userPhone = this.createFromForm();
    if (userPhone.id !== undefined) {
      this.subscribeToSaveResponse(this.userPhoneService.update(userPhone));
    } else {
      this.subscribeToSaveResponse(this.userPhoneService.create(userPhone));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserPhone>>): void {
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

  protected updateForm(userPhone: IUserPhone): void {
    this.editForm.patchValue({
      id: userPhone.id,
      phoneNumber: userPhone.phoneNumber,
      verified: userPhone.verified,
      internalUser: userPhone.internalUser,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, userPhone.internalUser);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('internalUser')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IUserPhone {
    return {
      ...new UserPhone(),
      id: this.editForm.get(['id'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      verified: this.editForm.get(['verified'])!.value,
      internalUser: this.editForm.get(['internalUser'])!.value,
    };
  }
}
