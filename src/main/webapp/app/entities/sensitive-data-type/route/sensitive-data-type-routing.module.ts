import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SensitiveDataTypeComponent } from '../list/sensitive-data-type.component';
import { SensitiveDataTypeDetailComponent } from '../detail/sensitive-data-type-detail.component';
import { SensitiveDataTypeUpdateComponent } from '../update/sensitive-data-type-update.component';
import { SensitiveDataTypeRoutingResolveService } from './sensitive-data-type-routing-resolve.service';

const sensitiveDataTypeRoute: Routes = [
  {
    path: '',
    component: SensitiveDataTypeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SensitiveDataTypeDetailComponent,
    resolve: {
      sensitiveDataType: SensitiveDataTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SensitiveDataTypeUpdateComponent,
    resolve: {
      sensitiveDataType: SensitiveDataTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SensitiveDataTypeUpdateComponent,
    resolve: {
      sensitiveDataType: SensitiveDataTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sensitiveDataTypeRoute)],
  exports: [RouterModule],
})
export class SensitiveDataTypeRoutingModule {}
