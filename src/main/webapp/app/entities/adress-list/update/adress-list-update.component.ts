import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAdressList, AdressList } from '../adress-list.model';
import { AdressListService } from '../service/adress-list.service';

@Component({
  selector: 'jhi-adress-list-update',
  templateUrl: './adress-list-update.component.html',
})
export class AdressListUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    street: [],
    streetSuite: [],
    postalCode: [],
    state: [],
    country: [],
    latitude: [],
    longitude: [],
  });

  constructor(protected adressListService: AdressListService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adressList }) => {
      this.updateForm(adressList);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const adressList = this.createFromForm();
    if (adressList.id !== undefined) {
      this.subscribeToSaveResponse(this.adressListService.update(adressList));
    } else {
      this.subscribeToSaveResponse(this.adressListService.create(adressList));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdressList>>): void {
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

  protected updateForm(adressList: IAdressList): void {
    this.editForm.patchValue({
      id: adressList.id,
      street: adressList.street,
      streetSuite: adressList.streetSuite,
      postalCode: adressList.postalCode,
      state: adressList.state,
      country: adressList.country,
      latitude: adressList.latitude,
      longitude: adressList.longitude,
    });
  }

  protected createFromForm(): IAdressList {
    return {
      ...new AdressList(),
      id: this.editForm.get(['id'])!.value,
      street: this.editForm.get(['street'])!.value,
      streetSuite: this.editForm.get(['streetSuite'])!.value,
      postalCode: this.editForm.get(['postalCode'])!.value,
      state: this.editForm.get(['state'])!.value,
      country: this.editForm.get(['country'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      longitude: this.editForm.get(['longitude'])!.value,
    };
  }
}
