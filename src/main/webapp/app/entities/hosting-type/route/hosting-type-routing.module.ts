import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HostingTypeComponent } from '../list/hosting-type.component';
import { HostingTypeDetailComponent } from '../detail/hosting-type-detail.component';
import { HostingTypeUpdateComponent } from '../update/hosting-type-update.component';
import { HostingTypeRoutingResolveService } from './hosting-type-routing-resolve.service';

const hostingTypeRoute: Routes = [
  {
    path: '',
    component: HostingTypeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HostingTypeDetailComponent,
    resolve: {
      hostingType: HostingTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HostingTypeUpdateComponent,
    resolve: {
      hostingType: HostingTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HostingTypeUpdateComponent,
    resolve: {
      hostingType: HostingTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(hostingTypeRoute)],
  exports: [RouterModule],
})
export class HostingTypeRoutingModule {}
