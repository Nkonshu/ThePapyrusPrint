import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ThePapyrusPrintSharedModule } from 'app/shared/shared.module';
import { PointComponent } from './point.component';
import { PointDetailComponent } from './point-detail.component';
import { PointUpdateComponent } from './point-update.component';
import { PointDeleteDialogComponent } from './point-delete-dialog.component';
import { pointRoute } from './point.route';

@NgModule({
  imports: [ThePapyrusPrintSharedModule, RouterModule.forChild(pointRoute)],
  declarations: [PointComponent, PointDetailComponent, PointUpdateComponent, PointDeleteDialogComponent],
  entryComponents: [PointDeleteDialogComponent]
})
export class ThePapyrusPrintPointModule {}
