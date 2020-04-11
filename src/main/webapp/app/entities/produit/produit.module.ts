import { NgModule, LOCALE_ID } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ThePapyrusPrintSharedModule } from 'app/shared/shared.module';
import { ProduitComponent } from './produit.component';
import { ProduitDetailComponent } from './produit-detail.component';
import { ProduitUpdateComponent } from './produit-update.component';
import { ProduitDeleteDialogComponent } from './produit-delete-dialog.component';
import { produitRoute } from './produit.route';
import { CartProduitComponent } from './cart-produit/cart-produit.component';
import { SpacePipe } from './currency-space.pipe';
import localeFr from '@angular/common/locales/fr';
import { registerLocaleData } from '@angular/common';

registerLocaleData(localeFr, 'fr');

@NgModule({
  imports: [ThePapyrusPrintSharedModule, RouterModule.forChild(produitRoute)],
  exports: [CartProduitComponent, SpacePipe],
  declarations: [
    SpacePipe,
    CartProduitComponent,
    ProduitComponent,
    ProduitDetailComponent,
    ProduitUpdateComponent,
    ProduitDeleteDialogComponent
  ],
  entryComponents: [ProduitDeleteDialogComponent],
  providers: [{ provide: LOCALE_ID, useValue: 'fr-FR' }]
})
export class ThePapyrusPrintProduitModule {}
