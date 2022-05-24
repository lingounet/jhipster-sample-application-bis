import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PsatComponent } from '../list/psat.component';
import { PsatDetailComponent } from '../detail/psat-detail.component';
import { PsatUpdateComponent } from '../update/psat-update.component';
import { PsatRoutingResolveService } from './psat-routing-resolve.service';

const psatRoute: Routes = [
  {
    path: '',
    component: PsatComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PsatDetailComponent,
    resolve: {
      psat: PsatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PsatUpdateComponent,
    resolve: {
      psat: PsatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PsatUpdateComponent,
    resolve: {
      psat: PsatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(psatRoute)],
  exports: [RouterModule],
})
export class PsatRoutingModule {}
