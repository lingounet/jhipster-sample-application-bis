import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IdentityComponent } from '../list/identity.component';
import { IdentityDetailComponent } from '../detail/identity-detail.component';
import { IdentityUpdateComponent } from '../update/identity-update.component';
import { IdentityRoutingResolveService } from './identity-routing-resolve.service';

const identityRoute: Routes = [
  {
    path: '',
    component: IdentityComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IdentityDetailComponent,
    resolve: {
      identity: IdentityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IdentityUpdateComponent,
    resolve: {
      identity: IdentityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IdentityUpdateComponent,
    resolve: {
      identity: IdentityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(identityRoute)],
  exports: [RouterModule],
})
export class IdentityRoutingModule {}
