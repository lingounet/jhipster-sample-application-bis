import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PersonalDataTypeComponent } from './list/personal-data-type.component';
import { PersonalDataTypeDetailComponent } from './detail/personal-data-type-detail.component';
import { PersonalDataTypeUpdateComponent } from './update/personal-data-type-update.component';
import { PersonalDataTypeDeleteDialogComponent } from './delete/personal-data-type-delete-dialog.component';
import { PersonalDataTypeRoutingModule } from './route/personal-data-type-routing.module';

@NgModule({
  imports: [SharedModule, PersonalDataTypeRoutingModule],
  declarations: [
    PersonalDataTypeComponent,
    PersonalDataTypeDetailComponent,
    PersonalDataTypeUpdateComponent,
    PersonalDataTypeDeleteDialogComponent,
  ],
  entryComponents: [PersonalDataTypeDeleteDialogComponent],
})
export class PersonalDataTypeModule {}
