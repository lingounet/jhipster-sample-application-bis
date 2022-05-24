import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIcrfAssessment } from '../icrf-assessment.model';
import { IcrfAssessmentService } from '../service/icrf-assessment.service';

@Component({
  templateUrl: './icrf-assessment-delete-dialog.component.html',
})
export class IcrfAssessmentDeleteDialogComponent {
  icrfAssessment?: IIcrfAssessment;

  constructor(protected icrfAssessmentService: IcrfAssessmentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.icrfAssessmentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
