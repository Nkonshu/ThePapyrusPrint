import { Route } from '@angular/router';

// import { HomeComponent } from './home.component';
// import { ProduitComponent } from '../entities/produit/produit.component';

export const HOME_ROUTE: Route = {
  path: '',
  redirectTo: 'produit',
  pathMatch: 'full',
  /* component: HomeComponent, */
  data: {
    authorities: [],
    pageTitle: 'home.title'
  }
};
