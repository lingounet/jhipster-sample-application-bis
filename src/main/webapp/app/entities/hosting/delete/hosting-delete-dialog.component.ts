import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHosting } from '../hosting.model';
import { HostingService } from '../service/hosting.service';

@Component({
  templateUrl: './hosting-delete-dialog.component.html',
})
export class HostingDeleteDialogComponent {
  hosting?: IHosting;

  constructor(protected hostingService: HostingService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.hostingService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
