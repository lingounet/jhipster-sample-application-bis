import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IcrfComponent } from './list/icrf.component';
import { IcrfDetailComponent } from './detail/icrf-detail.component';
import { IcrfUpdateComponent } from './update/icrf-update.component';
import { IcrfDeleteDialogComponent } from './delete/icrf-delete-dialog.component';
import { IcrfRoutingModule } from './route/icrf-routing.module';

@NgModule({
  imports: [SharedModule, IcrfRoutingModule],
  declarations: [IcrfComponent, IcrfDetailComponent, IcrfUpdateComponent, IcrfDeleteDialogComponent],
  entryComponents: [IcrfDeleteDialogComponent],
})
export class IcrfModule {}
