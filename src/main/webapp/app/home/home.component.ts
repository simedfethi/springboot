import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import {AdressListService} from 'app/entities/adress-list/service/adress-list.service';
import { Account } from 'app/core/auth/account.model';
import { IAdressList } from 'app/entities/adress-list/adress-list.model';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  lat = 51.678418;
  lng = 7.809007;
  adressLists?: IAdressList[];
  isLoading = false;

  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private router: Router
  ,protected adressListService: AdressListService) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
      this.loadAll();

  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
  loadAll(): void {
    this.isLoading = true;

    this.adressListService.query().subscribe(
      (res: HttpResponse<IAdressList[]>) => {
        this.isLoading = false;
        this.adressLists = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }
}
