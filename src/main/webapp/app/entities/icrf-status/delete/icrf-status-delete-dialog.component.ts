import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIcrfStatus } from '../icrf-status.model';
import { IcrfStatusService } from '../service/icrf-status.service';

@Component({
  templateUrl: './icrf-status-delete-dialog.component.html',
})
export class IcrfStatusDeleteDialogComponent {
  icrfStatus?: IIcrfStatus;

  constructor(protected icrfStatusService: IcrfStatusService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.icrfStatusService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
