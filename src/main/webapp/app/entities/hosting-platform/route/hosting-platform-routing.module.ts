import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HostingPlatformComponent } from '../list/hosting-platform.component';
import { HostingPlatformDetailComponent } from '../detail/hosting-platform-detail.component';
import { HostingPlatformUpdateComponent } from '../update/hosting-platform-update.component';
import { HostingPlatformRoutingResolveService } from './hosting-platform-routing-resolve.service';

const hostingPlatformRoute: Routes = [
  {
    path: '',
    component: HostingPlatformComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HostingPlatformDetailComponent,
    resolve: {
      hostingPlatform: HostingPlatformRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HostingPlatformUpdateComponent,
    resolve: {
      hostingPlatform: HostingPlatformRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HostingPlatformUpdateComponent,
    resolve: {
      hostingPlatform: HostingPlatformRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(hostingPlatformRoute)],
  exports: [RouterModule],
})
export class HostingPlatformRoutingModule {}
