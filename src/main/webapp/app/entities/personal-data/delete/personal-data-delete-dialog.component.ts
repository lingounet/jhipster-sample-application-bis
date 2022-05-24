import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonalData } from '../personal-data.model';
import { PersonalDataService } from '../service/personal-data.service';

@Component({
  templateUrl: './personal-data-delete-dialog.component.html',
})
export class PersonalDataDeleteDialogComponent {
  personalData?: IPersonalData;

  constructor(protected personalDataService: PersonalDataService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personalDataService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
