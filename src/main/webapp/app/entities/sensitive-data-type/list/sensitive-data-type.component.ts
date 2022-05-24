import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISensitiveDataType } from '../sensitive-data-type.model';
import { SensitiveDataTypeService } from '../service/sensitive-data-type.service';
import { SensitiveDataTypeDeleteDialogComponent } from '../delete/sensitive-data-type-delete-dialog.component';

@Component({
  selector: 'jhi-sensitive-data-type',
  templateUrl: './sensitive-data-type.component.html',
})
export class SensitiveDataTypeComponent implements OnInit {
  sensitiveDataTypes?: ISensitiveDataType[];
  isLoading = false;

  constructor(protected sensitiveDataTypeService: SensitiveDataTypeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.sensitiveDataTypeService.query().subscribe({
      next: (res: HttpResponse<ISensitiveDataType[]>) => {
        this.isLoading = false;
        this.sensitiveDataTypes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ISensitiveDataType): number {
    return item.id!;
  }

  delete(sensitiveDataType: ISensitiveDataType): void {
    const modalRef = this.modalService.open(SensitiveDataTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sensitiveDataType = sensitiveDataType;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
