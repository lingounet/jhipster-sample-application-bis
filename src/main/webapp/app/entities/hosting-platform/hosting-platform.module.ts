import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HostingPlatformComponent } from './list/hosting-platform.component';
import { HostingPlatformDetailComponent } from './detail/hosting-platform-detail.component';
import { HostingPlatformUpdateComponent } from './update/hosting-platform-update.component';
import { HostingPlatformDeleteDialogComponent } from './delete/hosting-platform-delete-dialog.component';
import { HostingPlatformRoutingModule } from './route/hosting-platform-routing.module';

@NgModule({
  imports: [SharedModule, HostingPlatformRoutingModule],
  declarations: [
    HostingPlatformComponent,
    HostingPlatformDetailComponent,
    HostingPlatformUpdateComponent,
    HostingPlatformDeleteDialogComponent,
  ],
  entryComponents: [HostingPlatformDeleteDialogComponent],
})
export class HostingPlatformModule {}
