import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISensitiveDataType } from '../sensitive-data-type.model';
import { SensitiveDataTypeService } from '../service/sensitive-data-type.service';

@Component({
  templateUrl: './sensitive-data-type-delete-dialog.component.html',
})
export class SensitiveDataTypeDeleteDialogComponent {
  sensitiveDataType?: ISensitiveDataType;

  constructor(protected sensitiveDataTypeService: SensitiveDataTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sensitiveDataTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
