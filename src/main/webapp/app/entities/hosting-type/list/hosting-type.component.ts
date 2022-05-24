import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IHostingType } from '../hosting-type.model';
import { HostingTypeService } from '../service/hosting-type.service';
import { HostingTypeDeleteDialogComponent } from '../delete/hosting-type-delete-dialog.component';

@Component({
  selector: 'jhi-hosting-type',
  templateUrl: './hosting-type.component.html',
})
export class HostingTypeComponent implements OnInit {
  hostingTypes?: IHostingType[];
  isLoading = false;

  constructor(protected hostingTypeService: HostingTypeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.hostingTypeService.query().subscribe({
      next: (res: HttpResponse<IHostingType[]>) => {
        this.isLoading = false;
        this.hostingTypes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IHostingType): number {
    return item.id!;
  }

  delete(hostingType: IHostingType): void {
    const modalRef = this.modalService.open(HostingTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.hostingType = hostingType;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
