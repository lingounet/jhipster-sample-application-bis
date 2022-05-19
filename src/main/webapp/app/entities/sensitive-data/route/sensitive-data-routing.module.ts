import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SensitiveDataComponent } from '../list/sensitive-data.component';
import { SensitiveDataDetailComponent } from '../detail/sensitive-data-detail.component';
import { SensitiveDataUpdateComponent } from '../update/sensitive-data-update.component';
import { SensitiveDataRoutingResolveService } from './sensitive-data-routing-resolve.service';

const sensitiveDataRoute: Routes = [
  {
    path: '',
    component: SensitiveDataComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SensitiveDataDetailComponent,
    resolve: {
      sensitiveData: SensitiveDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SensitiveDataUpdateComponent,
    resolve: {
      sensitiveData: SensitiveDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SensitiveDataUpdateComponent,
    resolve: {
      sensitiveData: SensitiveDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sensitiveDataRoute)],
  exports: [RouterModule],
})
export class SensitiveDataRoutingModule {}
