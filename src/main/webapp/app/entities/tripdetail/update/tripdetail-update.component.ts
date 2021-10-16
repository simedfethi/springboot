import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITripdetail, Tripdetail } from '../tripdetail.model';
import { TripdetailService } from '../service/tripdetail.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ITripCategory } from 'app/entities/trip-category/trip-category.model';
import { TripCategoryService } from 'app/entities/trip-category/service/trip-category.service';

@Component({
  selector: 'jhi-tripdetail-update',
  templateUrl: './tripdetail-update.component.html',
})
export class TripdetailUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  tripCategoriesSharedCollection: ITripCategory[] = [];

  editForm = this.fb.group({
    id: [],
    minimumList: [],
    maximumList: [],
    createdDate: [],
    lastupdated: [],
    departureDate: [],
    arrivalDate: [],
    contentDate: [],
    tripmaster: [],
    category: [],
  });

  constructor(
    protected tripdetailService: TripdetailService,
    protected userService: UserService,
    protected tripCategoryService: TripCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tripdetail }) => {
      if (tripdetail.id === undefined) {
        const today = dayjs().startOf('day');
        tripdetail.contentDate = today;
      }

      this.updateForm(tripdetail);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tripdetail = this.createFromForm();
    if (tripdetail.id !== undefined) {
      this.subscribeToSaveResponse(this.tripdetailService.update(tripdetail));
    } else {
      this.subscribeToSaveResponse(this.tripdetailService.create(tripdetail));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackTripCategoryById(index: number, item: ITripCategory): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITripdetail>>): void {
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

  protected updateForm(tripdetail: ITripdetail): void {
    this.editForm.patchValue({
      id: tripdetail.id,
      minimumList: tripdetail.minimumList,
      maximumList: tripdetail.maximumList,
      createdDate: tripdetail.createdDate,
      lastupdated: tripdetail.lastupdated,
      departureDate: tripdetail.departureDate,
      arrivalDate: tripdetail.arrivalDate,
      contentDate: tripdetail.contentDate ? tripdetail.contentDate.format(DATE_TIME_FORMAT) : null,
      tripmaster: tripdetail.tripmaster,
      category: tripdetail.category,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, tripdetail.tripmaster);
    this.tripCategoriesSharedCollection = this.tripCategoryService.addTripCategoryToCollectionIfMissing(
      this.tripCategoriesSharedCollection,
      tripdetail.category
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('tripmaster')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.tripCategoryService
      .query()
      .pipe(map((res: HttpResponse<ITripCategory[]>) => res.body ?? []))
      .pipe(
        map((tripCategories: ITripCategory[]) =>
          this.tripCategoryService.addTripCategoryToCollectionIfMissing(tripCategories, this.editForm.get('category')!.value)
        )
      )
      .subscribe((tripCategories: ITripCategory[]) => (this.tripCategoriesSharedCollection = tripCategories));
  }

  protected createFromForm(): ITripdetail {
    return {
      ...new Tripdetail(),
      id: this.editForm.get(['id'])!.value,
      minimumList: this.editForm.get(['minimumList'])!.value,
      maximumList: this.editForm.get(['maximumList'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value,
      lastupdated: this.editForm.get(['lastupdated'])!.value,
      departureDate: this.editForm.get(['departureDate'])!.value,
      arrivalDate: this.editForm.get(['arrivalDate'])!.value,
      contentDate: this.editForm.get(['contentDate'])!.value
        ? dayjs(this.editForm.get(['contentDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      tripmaster: this.editForm.get(['tripmaster'])!.value,
      category: this.editForm.get(['category'])!.value,
    };
  }
}
