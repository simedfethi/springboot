import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITripCategory, TripCategory } from '../trip-category.model';
import { TripCategoryService } from '../service/trip-category.service';

@Component({
  selector: 'jhi-trip-category-update',
  templateUrl: './trip-category-update.component.html',
})
export class TripCategoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    categoryname: [null, [Validators.maxLength(50)]],
  });

  constructor(protected tripCategoryService: TripCategoryService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tripCategory }) => {
      this.updateForm(tripCategory);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tripCategory = this.createFromForm();
    if (tripCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.tripCategoryService.update(tripCategory));
    } else {
      this.subscribeToSaveResponse(this.tripCategoryService.create(tripCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITripCategory>>): void {
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

  protected updateForm(tripCategory: ITripCategory): void {
    this.editForm.patchValue({
      id: tripCategory.id,
      categoryname: tripCategory.categoryname,
    });
  }

  protected createFromForm(): ITripCategory {
    return {
      ...new TripCategory(),
      id: this.editForm.get(['id'])!.value,
      categoryname: this.editForm.get(['categoryname'])!.value,
    };
  }
}
