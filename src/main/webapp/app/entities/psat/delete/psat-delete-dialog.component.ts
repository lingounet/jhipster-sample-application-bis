import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPsat } from '../psat.model';
import { PsatService } from '../service/psat.service';

@Component({
  templateUrl: './psat-delete-dialog.component.html',
})
export class PsatDeleteDialogComponent {
  psat?: IPsat;

  constructor(protected psatService: PsatService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.psatService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
