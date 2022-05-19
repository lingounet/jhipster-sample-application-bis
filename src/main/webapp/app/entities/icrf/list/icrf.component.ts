import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IIcrf } from '../icrf.model';
import { IcrfService } from '../service/icrf.service';
import { IcrfDeleteDialogComponent } from '../delete/icrf-delete-dialog.component';

@Component({
  selector: 'jhi-icrf',
  templateUrl: './icrf.component.html',
})
export class IcrfComponent implements OnInit {
  icrfs?: IIcrf[];
  isLoading = false;

  constructor(protected icrfService: IcrfService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.icrfService.query().subscribe({
      next: (res: HttpResponse<IIcrf[]>) => {
        this.isLoading = false;
        this.icrfs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IIcrf): number {
    return item.id!;
  }

  delete(icrf: IIcrf): void {
    const modalRef = this.modalService.open(IcrfDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.icrf = icrf;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
