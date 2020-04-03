import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFacture, Facture } from 'app/shared/model/facture.model';
import { FactureService } from './facture.service';
import { FactureComponent } from './facture.component';
import { FactureDetailComponent } from './facture-detail.component';
import { FactureUpdateComponent } from './facture-update.component';

@Injectable({ providedIn: 'root' })
export class FactureResolve implements Resolve<IFacture> {
  constructor(private service: FactureService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFacture> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((facture: HttpResponse<Facture>) => {
          if (facture.body) {
            return of(facture.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Facture());
  }
}

export const factureRoute: Routes = [
  {
    path: '',
    component: FactureComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'thePapyrusPrintApp.facture.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FactureDetailComponent,
    resolve: {
      facture: FactureResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'thePapyrusPrintApp.facture.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FactureUpdateComponent,
    resolve: {
      facture: FactureResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'thePapyrusPrintApp.facture.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FactureUpdateComponent,
    resolve: {
      facture: FactureResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'thePapyrusPrintApp.facture.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
