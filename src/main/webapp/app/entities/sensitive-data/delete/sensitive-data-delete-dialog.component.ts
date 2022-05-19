import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISensitiveData } from '../sensitive-data.model';
import { SensitiveDataService } from '../service/sensitive-data.service';

@Component({
  templateUrl: './sensitive-data-delete-dialog.component.html',
})
export class SensitiveDataDeleteDialogComponent {
  sensitiveData?: ISensitiveData;

  constructor(protected sensitiveDataService: SensitiveDataService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sensitiveDataService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
