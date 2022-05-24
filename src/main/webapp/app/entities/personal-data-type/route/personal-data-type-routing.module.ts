import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonalDataTypeComponent } from '../list/personal-data-type.component';
import { PersonalDataTypeDetailComponent } from '../detail/personal-data-type-detail.component';
import { PersonalDataTypeUpdateComponent } from '../update/personal-data-type-update.component';
import { PersonalDataTypeRoutingResolveService } from './personal-data-type-routing-resolve.service';

const personalDataTypeRoute: Routes = [
  {
    path: '',
    component: PersonalDataTypeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonalDataTypeDetailComponent,
    resolve: {
      personalDataType: PersonalDataTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonalDataTypeUpdateComponent,
    resolve: {
      personalDataType: PersonalDataTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonalDataTypeUpdateComponent,
    resolve: {
      personalDataType: PersonalDataTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personalDataTypeRoute)],
  exports: [RouterModule],
})
export class PersonalDataTypeRoutingModule {}
