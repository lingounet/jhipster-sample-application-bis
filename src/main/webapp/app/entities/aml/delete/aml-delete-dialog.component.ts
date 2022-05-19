import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAml } from '../aml.model';
import { AmlService } from '../service/aml.service';

@Component({
  templateUrl: './aml-delete-dialog.component.html',
})
export class AmlDeleteDialogComponent {
  aml?: IAml;

  constructor(protected amlService: AmlService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.amlService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
