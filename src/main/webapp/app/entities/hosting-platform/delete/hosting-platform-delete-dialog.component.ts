import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHostingPlatform } from '../hosting-platform.model';
import { HostingPlatformService } from '../service/hosting-platform.service';

@Component({
  templateUrl: './hosting-platform-delete-dialog.component.html',
})
export class HostingPlatformDeleteDialogComponent {
  hostingPlatform?: IHostingPlatform;

  constructor(protected hostingPlatformService: HostingPlatformService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.hostingPlatformService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
