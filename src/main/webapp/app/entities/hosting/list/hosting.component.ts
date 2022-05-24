import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IHosting } from '../hosting.model';
import { HostingService } from '../service/hosting.service';
import { HostingDeleteDialogComponent } from '../delete/hosting-delete-dialog.component';

@Component({
  selector: 'jhi-hosting',
  templateUrl: './hosting.component.html',
})
export class HostingComponent implements OnInit {
  hostings?: IHosting[];
  isLoading = false;

  constructor(protected hostingService: HostingService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.hostingService.query().subscribe({
      next: (res: HttpResponse<IHosting[]>) => {
        this.isLoading = false;
        this.hostings = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IHosting): number {
    return item.id!;
  }

  delete(hosting: IHosting): void {
    const modalRef = this.modalService.open(HostingDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.hosting = hosting;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
