import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIdentity } from '../identity.model';
import { IdentityService } from '../service/identity.service';

@Component({
  templateUrl: './identity-delete-dialog.component.html',
})
export class IdentityDeleteDialogComponent {
  identity?: IIdentity;

  constructor(protected identityService: IdentityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.identityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
