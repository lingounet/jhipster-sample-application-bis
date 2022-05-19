import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonalDataRegionComponent } from '../list/personal-data-region.component';
import { PersonalDataRegionDetailComponent } from '../detail/personal-data-region-detail.component';
import { PersonalDataRegionUpdateComponent } from '../update/personal-data-region-update.component';
import { PersonalDataRegionRoutingResolveService } from './personal-data-region-routing-resolve.service';

const personalDataRegionRoute: Routes = [
  {
    path: '',
    component: PersonalDataRegionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonalDataRegionDetailComponent,
    resolve: {
      personalDataRegion: PersonalDataRegionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonalDataRegionUpdateComponent,
    resolve: {
      personalDataRegion: PersonalDataRegionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonalDataRegionUpdateComponent,
    resolve: {
      personalDataRegion: PersonalDataRegionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personalDataRegionRoute)],
  exports: [RouterModule],
})
export class PersonalDataRegionRoutingModule {}
