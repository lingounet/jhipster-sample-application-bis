import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IcrfAssessmentComponent } from '../list/icrf-assessment.component';
import { IcrfAssessmentDetailComponent } from '../detail/icrf-assessment-detail.component';
import { IcrfAssessmentUpdateComponent } from '../update/icrf-assessment-update.component';
import { IcrfAssessmentRoutingResolveService } from './icrf-assessment-routing-resolve.service';

const icrfAssessmentRoute: Routes = [
  {
    path: '',
    component: IcrfAssessmentComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IcrfAssessmentDetailComponent,
    resolve: {
      icrfAssessment: IcrfAssessmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IcrfAssessmentUpdateComponent,
    resolve: {
      icrfAssessment: IcrfAssessmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IcrfAssessmentUpdateComponent,
    resolve: {
      icrfAssessment: IcrfAssessmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(icrfAssessmentRoute)],
  exports: [RouterModule],
})
export class IcrfAssessmentRoutingModule {}
