import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonalDataType } from '../personal-data-type.model';
import { PersonalDataTypeService } from '../service/personal-data-type.service';

@Component({
  templateUrl: './personal-data-type-delete-dialog.component.html',
})
export class PersonalDataTypeDeleteDialogComponent {
  personalDataType?: IPersonalDataType;

  constructor(protected personalDataTypeService: PersonalDataTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personalDataTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
