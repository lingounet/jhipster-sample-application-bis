import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AvailabilityComponent } from './list/availability.component';
import { AvailabilityDetailComponent } from './detail/availability-detail.component';
import { AvailabilityUpdateComponent } from './update/availability-update.component';
import { AvailabilityDeleteDialogComponent } from './delete/availability-delete-dialog.component';
import { AvailabilityRoutingModule } from './route/availability-routing.module';

@NgModule({
  imports: [SharedModule, AvailabilityRoutingModule],
  declarations: [AvailabilityComponent, AvailabilityDetailComponent, AvailabilityUpdateComponent, AvailabilityDeleteDialogComponent],
  entryComponents: [AvailabilityDeleteDialogComponent],
})
export class AvailabilityModule {}
