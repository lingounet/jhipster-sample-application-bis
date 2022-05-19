import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonalDataComponent } from '../list/personal-data.component';
import { PersonalDataDetailComponent } from '../detail/personal-data-detail.component';
import { PersonalDataUpdateComponent } from '../update/personal-data-update.component';
import { PersonalDataRoutingResolveService } from './personal-data-routing-resolve.service';

const personalDataRoute: Routes = [
  {
    path: '',
    component: PersonalDataComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonalDataDetailComponent,
    resolve: {
      personalData: PersonalDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonalDataUpdateComponent,
    resolve: {
      personalData: PersonalDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonalDataUpdateComponent,
    resolve: {
      personalData: PersonalDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personalDataRoute)],
  exports: [RouterModule],
})
export class PersonalDataRoutingModule {}
