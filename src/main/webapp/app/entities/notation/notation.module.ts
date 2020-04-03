import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ThePapyrusPrintSharedModule } from 'app/shared/shared.module';
import { NotationComponent } from './notation.component';
import { NotationDetailComponent } from './notation-detail.component';
import { NotationUpdateComponent } from './notation-update.component';
import { NotationDeleteDialogComponent } from './notation-delete-dialog.component';
import { notationRoute } from './notation.route';

@NgModule({
  imports: [ThePapyrusPrintSharedModule, RouterModule.forChild(notationRoute)],
  declarations: [NotationComponent, NotationDetailComponent, NotationUpdateComponent, NotationDeleteDialogComponent],
  entryComponents: [NotationDeleteDialogComponent]
})
export class ThePapyrusPrintNotationModule {}
