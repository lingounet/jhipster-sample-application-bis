import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PersonalDataComponent } from './list/personal-data.component';
import { PersonalDataDetailComponent } from './detail/personal-data-detail.component';
import { PersonalDataUpdateComponent } from './update/personal-data-update.component';
import { PersonalDataDeleteDialogComponent } from './delete/personal-data-delete-dialog.component';
import { PersonalDataRoutingModule } from './route/personal-data-routing.module';

@NgModule({
  imports: [SharedModule, PersonalDataRoutingModule],
  declarations: [PersonalDataComponent, PersonalDataDetailComponent, PersonalDataUpdateComponent, PersonalDataDeleteDialogComponent],
  entryComponents: [PersonalDataDeleteDialogComponent],
})
export class PersonalDataModule {}
