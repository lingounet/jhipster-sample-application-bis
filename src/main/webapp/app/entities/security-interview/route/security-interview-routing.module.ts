import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SecurityInterviewComponent } from '../list/security-interview.component';
import { SecurityInterviewDetailComponent } from '../detail/security-interview-detail.component';
import { SecurityInterviewUpdateComponent } from '../update/security-interview-update.component';
import { SecurityInterviewRoutingResolveService } from './security-interview-routing-resolve.service';

const securityInterviewRoute: Routes = [
  {
    path: '',
    component: SecurityInterviewComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SecurityInterviewDetailComponent,
    resolve: {
      securityInterview: SecurityInterviewRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SecurityInterviewUpdateComponent,
    resolve: {
      securityInterview: SecurityInterviewRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SecurityInterviewUpdateComponent,
    resolve: {
      securityInterview: SecurityInterviewRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(securityInterviewRoute)],
  exports: [RouterModule],
})
export class SecurityInterviewRoutingModule {}
