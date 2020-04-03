import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ThePapyrusPrintSharedModule } from 'app/shared/shared.module';
import { MarqueComponent } from './marque.component';
import { MarqueDetailComponent } from './marque-detail.component';
import { MarqueUpdateComponent } from './marque-update.component';
import { MarqueDeleteDialogComponent } from './marque-delete-dialog.component';
import { marqueRoute } from './marque.route';

@NgModule({
  imports: [ThePapyrusPrintSharedModule, RouterModule.forChild(marqueRoute)],
  declarations: [MarqueComponent, MarqueDetailComponent, MarqueUpdateComponent, MarqueDeleteDialogComponent],
  entryComponents: [MarqueDeleteDialogComponent]
})
export class ThePapyrusPrintMarqueModule {}
