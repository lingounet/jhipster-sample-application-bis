import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IHostingPlatform } from '../hosting-platform.model';
import { HostingPlatformService } from '../service/hosting-platform.service';
import { HostingPlatformDeleteDialogComponent } from '../delete/hosting-platform-delete-dialog.component';

@Component({
  selector: 'jhi-hosting-platform',
  templateUrl: './hosting-platform.component.html',
})
export class HostingPlatformComponent implements OnInit {
  hostingPlatforms?: IHostingPlatform[];
  isLoading = false;

  constructor(protected hostingPlatformService: HostingPlatformService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.hostingPlatformService.query().subscribe({
      next: (res: HttpResponse<IHostingPlatform[]>) => {
        this.isLoading = false;
        this.hostingPlatforms = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IHostingPlatform): number {
    return item.id!;
  }

  delete(hostingPlatform: IHostingPlatform): void {
    const modalRef = this.modalService.open(HostingPlatformDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.hostingPlatform = hostingPlatform;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
