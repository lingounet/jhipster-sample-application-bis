import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIcrf } from '../icrf.model';
import { IcrfService } from '../service/icrf.service';

@Component({
  templateUrl: './icrf-delete-dialog.component.html',
})
export class IcrfDeleteDialogComponent {
  icrf?: IIcrf;

  constructor(protected icrfService: IcrfService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.icrfService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
