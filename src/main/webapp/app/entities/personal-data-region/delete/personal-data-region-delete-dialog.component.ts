import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonalDataRegion } from '../personal-data-region.model';
import { PersonalDataRegionService } from '../service/personal-data-region.service';

@Component({
  templateUrl: './personal-data-region-delete-dialog.component.html',
})
export class PersonalDataRegionDeleteDialogComponent {
  personalDataRegion?: IPersonalDataRegion;

  constructor(protected personalDataRegionService: PersonalDataRegionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personalDataRegionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
