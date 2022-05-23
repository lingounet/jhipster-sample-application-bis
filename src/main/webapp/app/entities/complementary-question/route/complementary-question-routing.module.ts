import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ComplementaryQuestionComponent } from '../list/complementary-question.component';
import { ComplementaryQuestionDetailComponent } from '../detail/complementary-question-detail.component';
import { ComplementaryQuestionUpdateComponent } from '../update/complementary-question-update.component';
import { ComplementaryQuestionRoutingResolveService } from './complementary-question-routing-resolve.service';

const complementaryQuestionRoute: Routes = [
  {
    path: '',
    component: ComplementaryQuestionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ComplementaryQuestionDetailComponent,
    resolve: {
      complementaryQuestion: ComplementaryQuestionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ComplementaryQuestionUpdateComponent,
    resolve: {
      complementaryQuestion: ComplementaryQuestionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ComplementaryQuestionUpdateComponent,
    resolve: {
      complementaryQuestion: ComplementaryQuestionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(complementaryQuestionRoute)],
  exports: [RouterModule],
})
export class ComplementaryQuestionRoutingModule {}
