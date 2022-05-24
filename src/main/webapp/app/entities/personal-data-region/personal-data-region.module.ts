import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PersonalDataRegionComponent } from './list/personal-data-region.component';
import { PersonalDataRegionDetailComponent } from './detail/personal-data-region-detail.component';
import { PersonalDataRegionUpdateComponent } from './update/personal-data-region-update.component';
import { PersonalDataRegionDeleteDialogComponent } from './delete/personal-data-region-delete-dialog.component';
import { PersonalDataRegionRoutingModule } from './route/personal-data-region-routing.module';

@NgModule({
  imports: [SharedModule, PersonalDataRegionRoutingModule],
  declarations: [
    PersonalDataRegionComponent,
    PersonalDataRegionDetailComponent,
    PersonalDataRegionUpdateComponent,
    PersonalDataRegionDeleteDialogComponent,
  ],
  entryComponents: [PersonalDataRegionDeleteDialogComponent],
})
export class PersonalDataRegionModule {}
