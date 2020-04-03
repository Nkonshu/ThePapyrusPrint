import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'commande',
        loadChildren: () => import('./commande/commande.module').then(m => m.ThePapyrusPrintCommandeModule)
      },
      {
        path: 'facture',
        loadChildren: () => import('./facture/facture.module').then(m => m.ThePapyrusPrintFactureModule)
      },
      {
        path: 'fournisseur',
        loadChildren: () => import('./fournisseur/fournisseur.module').then(m => m.ThePapyrusPrintFournisseurModule)
      },
      {
        path: 'stock',
        loadChildren: () => import('./stock/stock.module').then(m => m.ThePapyrusPrintStockModule)
      },
      {
        path: 'catalogue',
        loadChildren: () => import('./catalogue/catalogue.module').then(m => m.ThePapyrusPrintCatalogueModule)
      },
      {
        path: 'produit',
        loadChildren: () => import('./produit/produit.module').then(m => m.ThePapyrusPrintProduitModule)
      },
      {
        path: 'notation',
        loadChildren: () => import('./notation/notation.module').then(m => m.ThePapyrusPrintNotationModule)
      },
      {
        path: 'marque',
        loadChildren: () => import('./marque/marque.module').then(m => m.ThePapyrusPrintMarqueModule)
      },
      {
        path: 'point',
        loadChildren: () => import('./point/point.module').then(m => m.ThePapyrusPrintPointModule)
      },
      {
        path: 'image',
        loadChildren: () => import('./image/image.module').then(m => m.ThePapyrusPrintImageModule)
      },
      {
        path: 'client',
        loadChildren: () => import('./client/client.module').then(m => m.ThePapyrusPrintClientModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class ThePapyrusPrintEntityModule {}
