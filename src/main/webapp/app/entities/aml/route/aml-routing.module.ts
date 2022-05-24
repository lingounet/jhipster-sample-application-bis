import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AmlComponent } from '../list/aml.component';
import { AmlDetailComponent } from '../detail/aml-detail.component';
import { AmlUpdateComponent } from '../update/aml-update.component';
import { AmlRoutingResolveService } from './aml-routing-resolve.service';

const amlRoute: Routes = [
  {
    path: '',
    component: AmlComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AmlDetailComponent,
    resolve: {
      aml: AmlRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AmlUpdateComponent,
    resolve: {
      aml: AmlRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AmlUpdateComponent,
    resolve: {
      aml: AmlRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(amlRoute)],
  exports: [RouterModule],
})
export class AmlRoutingModule {}
