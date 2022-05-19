import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IcrfStatusComponent } from '../list/icrf-status.component';
import { IcrfStatusDetailComponent } from '../detail/icrf-status-detail.component';
import { IcrfStatusUpdateComponent } from '../update/icrf-status-update.component';
import { IcrfStatusRoutingResolveService } from './icrf-status-routing-resolve.service';

const icrfStatusRoute: Routes = [
  {
    path: '',
    component: IcrfStatusComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IcrfStatusDetailComponent,
    resolve: {
      icrfStatus: IcrfStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IcrfStatusUpdateComponent,
    resolve: {
      icrfStatus: IcrfStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IcrfStatusUpdateComponent,
    resolve: {
      icrfStatus: IcrfStatusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(icrfStatusRoute)],
  exports: [RouterModule],
})
export class IcrfStatusRoutingModule {}
