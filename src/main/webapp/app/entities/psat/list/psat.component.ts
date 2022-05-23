import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPsat } from '../psat.model';
import { PsatService } from '../service/psat.service';
import { PsatDeleteDialogComponent } from '../delete/psat-delete-dialog.component';

@Component({
  selector: 'jhi-psat',
  templateUrl: './psat.component.html',
})
export class PsatComponent implements OnInit {
  psats?: IPsat[];
  isLoading = false;

  constructor(protected psatService: PsatService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.psatService.query().subscribe({
      next: (res: HttpResponse<IPsat[]>) => {
        this.isLoading = false;
        this.psats = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPsat): number {
    return item.id!;
  }

  delete(psat: IPsat): void {
    const modalRef = this.modalService.open(PsatDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.psat = psat;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
