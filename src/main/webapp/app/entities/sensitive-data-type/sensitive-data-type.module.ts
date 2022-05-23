import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SensitiveDataTypeComponent } from './list/sensitive-data-type.component';
import { SensitiveDataTypeDetailComponent } from './detail/sensitive-data-type-detail.component';
import { SensitiveDataTypeUpdateComponent } from './update/sensitive-data-type-update.component';
import { SensitiveDataTypeDeleteDialogComponent } from './delete/sensitive-data-type-delete-dialog.component';
import { SensitiveDataTypeRoutingModule } from './route/sensitive-data-type-routing.module';

@NgModule({
  imports: [SharedModule, SensitiveDataTypeRoutingModule],
  declarations: [
    SensitiveDataTypeComponent,
    SensitiveDataTypeDetailComponent,
    SensitiveDataTypeUpdateComponent,
    SensitiveDataTypeDeleteDialogComponent,
  ],
  entryComponents: [SensitiveDataTypeDeleteDialogComponent],
})
export class SensitiveDataTypeModule {}
