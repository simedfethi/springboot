import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUserGovernmentID, UserGovernmentID } from '../user-government-id.model';
import { UserGovernmentIDService } from '../service/user-government-id.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-user-government-id-update',
  templateUrl: './user-government-id-update.component.html',
})
export class UserGovernmentIDUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    country: [null, [Validators.maxLength(50)]],
    docType: [],
    docPicRect: [],
    docPicRectContentType: [],
    docPicVers: [],
    docPicVersContentType: [],
    docPicInst: [],
    docPicInstContentType: [],
    idVerified: [],
    user: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected userGovernmentIDService: UserGovernmentIDService,
    protected userService: UserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userGovernmentID }) => {
      this.updateForm(userGovernmentID);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('tripApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userGovernmentID = this.createFromForm();
    if (userGovernmentID.id !== undefined) {
      this.subscribeToSaveResponse(this.userGovernmentIDService.update(userGovernmentID));
    } else {
      this.subscribeToSaveResponse(this.userGovernmentIDService.create(userGovernmentID));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserGovernmentID>>): void {
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

  protected updateForm(userGovernmentID: IUserGovernmentID): void {
    this.editForm.patchValue({
      id: userGovernmentID.id,
      country: userGovernmentID.country,
      docType: userGovernmentID.docType,
      docPicRect: userGovernmentID.docPicRect,
      docPicRectContentType: userGovernmentID.docPicRectContentType,
      docPicVers: userGovernmentID.docPicVers,
      docPicVersContentType: userGovernmentID.docPicVersContentType,
      docPicInst: userGovernmentID.docPicInst,
      docPicInstContentType: userGovernmentID.docPicInstContentType,
      idVerified: userGovernmentID.idVerified,
      user: userGovernmentID.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, userGovernmentID.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IUserGovernmentID {
    return {
      ...new UserGovernmentID(),
      id: this.editForm.get(['id'])!.value,
      country: this.editForm.get(['country'])!.value,
      docType: this.editForm.get(['docType'])!.value,
      docPicRectContentType: this.editForm.get(['docPicRectContentType'])!.value,
      docPicRect: this.editForm.get(['docPicRect'])!.value,
      docPicVersContentType: this.editForm.get(['docPicVersContentType'])!.value,
      docPicVers: this.editForm.get(['docPicVers'])!.value,
      docPicInstContentType: this.editForm.get(['docPicInstContentType'])!.value,
      docPicInst: this.editForm.get(['docPicInst'])!.value,
      idVerified: this.editForm.get(['idVerified'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
