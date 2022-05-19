import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonalDataType } from '../personal-data-type.model';
import { PersonalDataTypeService } from '../service/personal-data-type.service';
import { PersonalDataTypeDeleteDialogComponent } from '../delete/personal-data-type-delete-dialog.component';

@Component({
  selector: 'jhi-personal-data-type',
  templateUrl: './personal-data-type.component.html',
})
export class PersonalDataTypeComponent implements OnInit {
  personalDataTypes?: IPersonalDataType[];
  isLoading = false;

  constructor(protected personalDataTypeService: PersonalDataTypeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.personalDataTypeService.query().subscribe({
      next: (res: HttpResponse<IPersonalDataType[]>) => {
        this.isLoading = false;
        this.personalDataTypes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPersonalDataType): number {
    return item.id!;
  }

  delete(personalDataType: IPersonalDataType): void {
    const modalRef = this.modalService.open(PersonalDataTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.personalDataType = personalDataType;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
