import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IcrfAssessmentComponent } from './list/icrf-assessment.component';
import { IcrfAssessmentDetailComponent } from './detail/icrf-assessment-detail.component';
import { IcrfAssessmentUpdateComponent } from './update/icrf-assessment-update.component';
import { IcrfAssessmentDeleteDialogComponent } from './delete/icrf-assessment-delete-dialog.component';
import { IcrfAssessmentRoutingModule } from './route/icrf-assessment-routing.module';

@NgModule({
  imports: [SharedModule, IcrfAssessmentRoutingModule],
  declarations: [
    IcrfAssessmentComponent,
    IcrfAssessmentDetailComponent,
    IcrfAssessmentUpdateComponent,
    IcrfAssessmentDeleteDialogComponent,
  ],
  entryComponents: [IcrfAssessmentDeleteDialogComponent],
})
export class IcrfAssessmentModule {}
