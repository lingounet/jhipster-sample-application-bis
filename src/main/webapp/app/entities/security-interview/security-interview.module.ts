import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SecurityInterviewComponent } from './list/security-interview.component';
import { SecurityInterviewDetailComponent } from './detail/security-interview-detail.component';
import { SecurityInterviewUpdateComponent } from './update/security-interview-update.component';
import { SecurityInterviewDeleteDialogComponent } from './delete/security-interview-delete-dialog.component';
import { SecurityInterviewRoutingModule } from './route/security-interview-routing.module';

@NgModule({
  imports: [SharedModule, SecurityInterviewRoutingModule],
  declarations: [
    SecurityInterviewComponent,
    SecurityInterviewDetailComponent,
    SecurityInterviewUpdateComponent,
    SecurityInterviewDeleteDialogComponent,
  ],
  entryComponents: [SecurityInterviewDeleteDialogComponent],
})
export class SecurityInterviewModule {}
