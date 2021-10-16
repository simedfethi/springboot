import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAdressList } from '../adress-list.model';

@Component({
  selector: 'jhi-adress-list-detail',
  templateUrl: './adress-list-detail.component.html',
})
export class AdressListDetailComponent implements OnInit {
  adressList: IAdressList | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adressList }) => {
      this.adressList = adressList;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
