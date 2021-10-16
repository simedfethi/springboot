import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { AgmCoreModule } from '@agm/core';
import { BrowserModule } from '@angular/platform-browser';
@NgModule({
  imports: [
    SharedModule,
    BrowserModule,
    RouterModule.forChild([HOME_ROUTE]),
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyDaiei37tf_uRvmxg5BiabT_qyS4GWiP04'
    })
  ],
  declarations: [HomeComponent],
})
export class HomeModule {}
