import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ThePapyrusPrintSharedModule } from 'app/shared/shared.module';
import { FactureComponent } from './facture.component';
import { FactureDetailComponent } from './facture-detail.component';
import { FactureUpdateComponent } from './facture-update.component';
import { FactureDeleteDialogComponent } from './facture-delete-dialog.component';
import { factureRoute } from './facture.route';

@NgModule({
  imports: [ThePapyrusPrintSharedModule, RouterModule.forChild(factureRoute)],
  declarations: [FactureComponent, FactureDetailComponent, FactureUpdateComponent, FactureDeleteDialogComponent],
  entryComponents: [FactureDeleteDialogComponent]
})
export class ThePapyrusPrintFactureModule {}
