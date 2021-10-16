import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AdressListComponent } from './list/adress-list.component';
import { AdressListDetailComponent } from './detail/adress-list-detail.component';
import { AdressListUpdateComponent } from './update/adress-list-update.component';
import { AdressListDeleteDialogComponent } from './delete/adress-list-delete-dialog.component';
import { AdressListRoutingModule } from './route/adress-list-routing.module';

@NgModule({
  imports: [SharedModule, AdressListRoutingModule],
  declarations: [AdressListComponent, AdressListDetailComponent, AdressListUpdateComponent, AdressListDeleteDialogComponent],
  entryComponents: [AdressListDeleteDialogComponent],
})
export class AdressListModule {}
