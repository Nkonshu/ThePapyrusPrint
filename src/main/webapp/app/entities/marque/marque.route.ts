import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMarque, Marque } from 'app/shared/model/marque.model';
import { MarqueService } from './marque.service';
import { MarqueComponent } from './marque.component';
import { MarqueDetailComponent } from './marque-detail.component';
import { MarqueUpdateComponent } from './marque-update.component';

@Injectable({ providedIn: 'root' })
export class MarqueResolve implements Resolve<IMarque> {
  constructor(private service: MarqueService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMarque> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((marque: HttpResponse<Marque>) => {
          if (marque.body) {
            return of(marque.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Marque());
  }
}

export const marqueRoute: Routes = [
  {
    path: '',
    component: MarqueComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'thePapyrusPrintApp.marque.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MarqueDetailComponent,
    resolve: {
      marque: MarqueResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'thePapyrusPrintApp.marque.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MarqueUpdateComponent,
    resolve: {
      marque: MarqueResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'thePapyrusPrintApp.marque.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MarqueUpdateComponent,
    resolve: {
      marque: MarqueResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'thePapyrusPrintApp.marque.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
