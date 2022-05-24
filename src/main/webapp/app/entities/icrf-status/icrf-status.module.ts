import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IcrfStatusComponent } from './list/icrf-status.component';
import { IcrfStatusDetailComponent } from './detail/icrf-status-detail.component';
import { IcrfStatusUpdateComponent } from './update/icrf-status-update.component';
import { IcrfStatusDeleteDialogComponent } from './delete/icrf-status-delete-dialog.component';
import { IcrfStatusRoutingModule } from './route/icrf-status-routing.module';

@NgModule({
  imports: [SharedModule, IcrfStatusRoutingModule],
  declarations: [IcrfStatusComponent, IcrfStatusDetailComponent, IcrfStatusUpdateComponent, IcrfStatusDeleteDialogComponent],
  entryComponents: [IcrfStatusDeleteDialogComponent],
})
export class IcrfStatusModule {}
