import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IApplicationType } from '../application-type.model';
import { ApplicationTypeService } from '../service/application-type.service';
import { ApplicationTypeDeleteDialogComponent } from '../delete/application-type-delete-dialog.component';

@Component({
  selector: 'jhi-application-type',
  templateUrl: './application-type.component.html',
})
export class ApplicationTypeComponent implements OnInit {
  applicationTypes?: IApplicationType[];
  isLoading = false;

  constructor(protected applicationTypeService: ApplicationTypeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.applicationTypeService.query().subscribe({
      next: (res: HttpResponse<IApplicationType[]>) => {
        this.isLoading = false;
        this.applicationTypes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IApplicationType): number {
    return item.id!;
  }

  delete(applicationType: IApplicationType): void {
    const modalRef = this.modalService.open(ApplicationTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.applicationType = applicationType;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
