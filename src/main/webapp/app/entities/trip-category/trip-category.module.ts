import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TripCategoryComponent } from './list/trip-category.component';
import { TripCategoryDetailComponent } from './detail/trip-category-detail.component';
import { TripCategoryUpdateComponent } from './update/trip-category-update.component';
import { TripCategoryDeleteDialogComponent } from './delete/trip-category-delete-dialog.component';
import { TripCategoryRoutingModule } from './route/trip-category-routing.module';

@NgModule({
  imports: [SharedModule, TripCategoryRoutingModule],
  declarations: [TripCategoryComponent, TripCategoryDetailComponent, TripCategoryUpdateComponent, TripCategoryDeleteDialogComponent],
  entryComponents: [TripCategoryDeleteDialogComponent],
})
export class TripCategoryModule {}
