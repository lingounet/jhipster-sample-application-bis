import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHostingType } from '../hosting-type.model';
import { HostingTypeService } from '../service/hosting-type.service';

@Component({
  templateUrl: './hosting-type-delete-dialog.component.html',
})
export class HostingTypeDeleteDialogComponent {
  hostingType?: IHostingType;

  constructor(protected hostingTypeService: HostingTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.hostingTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
