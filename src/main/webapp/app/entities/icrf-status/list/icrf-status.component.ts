import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IIcrfStatus } from '../icrf-status.model';
import { IcrfStatusService } from '../service/icrf-status.service';
import { IcrfStatusDeleteDialogComponent } from '../delete/icrf-status-delete-dialog.component';

@Component({
  selector: 'jhi-icrf-status',
  templateUrl: './icrf-status.component.html',
})
export class IcrfStatusComponent implements OnInit {
  icrfStatuses?: IIcrfStatus[];
  isLoading = false;

  constructor(protected icrfStatusService: IcrfStatusService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.icrfStatusService.query().subscribe({
      next: (res: HttpResponse<IIcrfStatus[]>) => {
        this.isLoading = false;
        this.icrfStatuses = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IIcrfStatus): number {
    return item.id!;
  }

  delete(icrfStatus: IIcrfStatus): void {
    const modalRef = this.modalService.open(IcrfStatusDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.icrfStatus = icrfStatus;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
