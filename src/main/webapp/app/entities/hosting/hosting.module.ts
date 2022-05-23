import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HostingComponent } from './list/hosting.component';
import { HostingDetailComponent } from './detail/hosting-detail.component';
import { HostingUpdateComponent } from './update/hosting-update.component';
import { HostingDeleteDialogComponent } from './delete/hosting-delete-dialog.component';
import { HostingRoutingModule } from './route/hosting-routing.module';

@NgModule({
  imports: [SharedModule, HostingRoutingModule],
  declarations: [HostingComponent, HostingDetailComponent, HostingUpdateComponent, HostingDeleteDialogComponent],
  entryComponents: [HostingDeleteDialogComponent],
})
export class HostingModule {}
