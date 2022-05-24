import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HostingComponent } from '../list/hosting.component';
import { HostingDetailComponent } from '../detail/hosting-detail.component';
import { HostingUpdateComponent } from '../update/hosting-update.component';
import { HostingRoutingResolveService } from './hosting-routing-resolve.service';

const hostingRoute: Routes = [
  {
    path: '',
    component: HostingComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HostingDetailComponent,
    resolve: {
      hosting: HostingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HostingUpdateComponent,
    resolve: {
      hosting: HostingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HostingUpdateComponent,
    resolve: {
      hosting: HostingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(hostingRoute)],
  exports: [RouterModule],
})
export class HostingRoutingModule {}
