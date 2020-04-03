import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { INotation, Notation } from 'app/shared/model/notation.model';
import { NotationService } from './notation.service';
import { NotationComponent } from './notation.component';
import { NotationDetailComponent } from './notation-detail.component';
import { NotationUpdateComponent } from './notation-update.component';

@Injectable({ providedIn: 'root' })
export class NotationResolve implements Resolve<INotation> {
  constructor(private service: NotationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INotation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((notation: HttpResponse<Notation>) => {
          if (notation.body) {
            return of(notation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Notation());
  }
}

export const notationRoute: Routes = [
  {
    path: '',
    component: NotationComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'thePapyrusPrintApp.notation.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: NotationDetailComponent,
    resolve: {
      notation: NotationResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'thePapyrusPrintApp.notation.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: NotationUpdateComponent,
    resolve: {
      notation: NotationResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'thePapyrusPrintApp.notation.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: NotationUpdateComponent,
    resolve: {
      notation: NotationResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'thePapyrusPrintApp.notation.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
