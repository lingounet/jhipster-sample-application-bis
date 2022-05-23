import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ApplicationTypeComponent } from '../list/application-type.component';
import { ApplicationTypeDetailComponent } from '../detail/application-type-detail.component';
import { ApplicationTypeUpdateComponent } from '../update/application-type-update.component';
import { ApplicationTypeRoutingResolveService } from './application-type-routing-resolve.service';

const applicationTypeRoute: Routes = [
  {
    path: '',
    component: ApplicationTypeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApplicationTypeDetailComponent,
    resolve: {
      applicationType: ApplicationTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApplicationTypeUpdateComponent,
    resolve: {
      applicationType: ApplicationTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApplicationTypeUpdateComponent,
    resolve: {
      applicationType: ApplicationTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(applicationTypeRoute)],
  exports: [RouterModule],
})
export class ApplicationTypeRoutingModule {}
