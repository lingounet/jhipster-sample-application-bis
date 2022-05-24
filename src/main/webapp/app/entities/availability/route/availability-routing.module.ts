import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AvailabilityComponent } from '../list/availability.component';
import { AvailabilityDetailComponent } from '../detail/availability-detail.component';
import { AvailabilityUpdateComponent } from '../update/availability-update.component';
import { AvailabilityRoutingResolveService } from './availability-routing-resolve.service';

const availabilityRoute: Routes = [
  {
    path: '',
    component: AvailabilityComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AvailabilityDetailComponent,
    resolve: {
      availability: AvailabilityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AvailabilityUpdateComponent,
    resolve: {
      availability: AvailabilityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AvailabilityUpdateComponent,
    resolve: {
      availability: AvailabilityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(availabilityRoute)],
  exports: [RouterModule],
})
export class AvailabilityRoutingModule {}
