import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAvailability } from '../availability.model';
import { AvailabilityService } from '../service/availability.service';

@Component({
  templateUrl: './availability-delete-dialog.component.html',
})
export class AvailabilityDeleteDialogComponent {
  availability?: IAvailability;

  constructor(protected availabilityService: AvailabilityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.availabilityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
