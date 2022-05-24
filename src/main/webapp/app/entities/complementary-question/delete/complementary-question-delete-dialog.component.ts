import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IComplementaryQuestion } from '../complementary-question.model';
import { ComplementaryQuestionService } from '../service/complementary-question.service';

@Component({
  templateUrl: './complementary-question-delete-dialog.component.html',
})
export class ComplementaryQuestionDeleteDialogComponent {
  complementaryQuestion?: IComplementaryQuestion;

  constructor(protected complementaryQuestionService: ComplementaryQuestionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.complementaryQuestionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
