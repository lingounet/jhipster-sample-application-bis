import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAvailability } from '../availability.model';
import { AvailabilityService } from '../service/availability.service';
import { AvailabilityDeleteDialogComponent } from '../delete/availability-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-availability',
  templateUrl: './availability.component.html',
})
export class AvailabilityComponent implements OnInit {
  availabilities?: IAvailability[];
  isLoading = false;

  constructor(protected availabilityService: AvailabilityService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.availabilityService.query().subscribe({
      next: (res: HttpResponse<IAvailability[]>) => {
        this.isLoading = false;
        this.availabilities = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IAvailability): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(availability: IAvailability): void {
    const modalRef = this.modalService.open(AvailabilityDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.availability = availability;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
