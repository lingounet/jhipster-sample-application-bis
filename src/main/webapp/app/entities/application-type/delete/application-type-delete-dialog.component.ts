import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IApplicationType } from '../application-type.model';
import { ApplicationTypeService } from '../service/application-type.service';

@Component({
  templateUrl: './application-type-delete-dialog.component.html',
})
export class ApplicationTypeDeleteDialogComponent {
  applicationType?: IApplicationType;

  constructor(protected applicationTypeService: ApplicationTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.applicationTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
