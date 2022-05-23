import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IcrfComponent } from '../list/icrf.component';
import { IcrfDetailComponent } from '../detail/icrf-detail.component';
import { IcrfUpdateComponent } from '../update/icrf-update.component';
import { IcrfRoutingResolveService } from './icrf-routing-resolve.service';

const icrfRoute: Routes = [
  {
    path: '',
    component: IcrfComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IcrfDetailComponent,
    resolve: {
      icrf: IcrfRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IcrfUpdateComponent,
    resolve: {
      icrf: IcrfRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IcrfUpdateComponent,
    resolve: {
      icrf: IcrfRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(icrfRoute)],
  exports: [RouterModule],
})
export class IcrfRoutingModule {}
