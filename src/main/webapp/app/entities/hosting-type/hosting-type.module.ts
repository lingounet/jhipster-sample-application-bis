import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HostingTypeComponent } from './list/hosting-type.component';
import { HostingTypeDetailComponent } from './detail/hosting-type-detail.component';
import { HostingTypeUpdateComponent } from './update/hosting-type-update.component';
import { HostingTypeDeleteDialogComponent } from './delete/hosting-type-delete-dialog.component';
import { HostingTypeRoutingModule } from './route/hosting-type-routing.module';

@NgModule({
  imports: [SharedModule, HostingTypeRoutingModule],
  declarations: [HostingTypeComponent, HostingTypeDetailComponent, HostingTypeUpdateComponent, HostingTypeDeleteDialogComponent],
  entryComponents: [HostingTypeDeleteDialogComponent],
})
export class HostingTypeModule {}
