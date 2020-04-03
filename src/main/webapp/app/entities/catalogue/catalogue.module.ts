import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ThePapyrusPrintSharedModule } from 'app/shared/shared.module';
import { CatalogueComponent } from './catalogue.component';
import { CatalogueDetailComponent } from './catalogue-detail.component';
import { CatalogueUpdateComponent } from './catalogue-update.component';
import { CatalogueDeleteDialogComponent } from './catalogue-delete-dialog.component';
import { catalogueRoute } from './catalogue.route';

@NgModule({
  imports: [ThePapyrusPrintSharedModule, RouterModule.forChild(catalogueRoute)],
  declarations: [CatalogueComponent, CatalogueDetailComponent, CatalogueUpdateComponent, CatalogueDeleteDialogComponent],
  entryComponents: [CatalogueDeleteDialogComponent]
})
export class ThePapyrusPrintCatalogueModule {}
