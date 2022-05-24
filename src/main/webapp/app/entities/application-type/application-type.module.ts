import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ApplicationTypeComponent } from './list/application-type.component';
import { ApplicationTypeDetailComponent } from './detail/application-type-detail.component';
import { ApplicationTypeUpdateComponent } from './update/application-type-update.component';
import { ApplicationTypeDeleteDialogComponent } from './delete/application-type-delete-dialog.component';
import { ApplicationTypeRoutingModule } from './route/application-type-routing.module';

@NgModule({
  imports: [SharedModule, ApplicationTypeRoutingModule],
  declarations: [
    ApplicationTypeComponent,
    ApplicationTypeDetailComponent,
    ApplicationTypeUpdateComponent,
    ApplicationTypeDeleteDialogComponent,
  ],
  entryComponents: [ApplicationTypeDeleteDialogComponent],
})
export class ApplicationTypeModule {}
