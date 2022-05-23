import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISecurityInterview } from '../security-interview.model';
import { SecurityInterviewService } from '../service/security-interview.service';

@Component({
  templateUrl: './security-interview-delete-dialog.component.html',
})
export class SecurityInterviewDeleteDialogComponent {
  securityInterview?: ISecurityInterview;

  constructor(protected securityInterviewService: SecurityInterviewService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.securityInterviewService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
