import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ComplementaryQuestionComponent } from './list/complementary-question.component';
import { ComplementaryQuestionDetailComponent } from './detail/complementary-question-detail.component';
import { ComplementaryQuestionUpdateComponent } from './update/complementary-question-update.component';
import { ComplementaryQuestionDeleteDialogComponent } from './delete/complementary-question-delete-dialog.component';
import { ComplementaryQuestionRoutingModule } from './route/complementary-question-routing.module';

@NgModule({
  imports: [SharedModule, ComplementaryQuestionRoutingModule],
  declarations: [
    ComplementaryQuestionComponent,
    ComplementaryQuestionDetailComponent,
    ComplementaryQuestionUpdateComponent,
    ComplementaryQuestionDeleteDialogComponent,
  ],
  entryComponents: [ComplementaryQuestionDeleteDialogComponent],
})
export class ComplementaryQuestionModule {}
