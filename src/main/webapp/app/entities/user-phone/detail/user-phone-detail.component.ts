import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserPhone } from '../user-phone.model';

@Component({
  selector: 'jhi-user-phone-detail',
  templateUrl: './user-phone-detail.component.html',
})
export class UserPhoneDetailComponent implements OnInit {
  userPhone: IUserPhone | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userPhone }) => {
      this.userPhone = userPhone;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
