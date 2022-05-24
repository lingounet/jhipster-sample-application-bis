import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISensitiveData } from '../sensitive-data.model';
import { SensitiveDataService } from '../service/sensitive-data.service';
import { SensitiveDataDeleteDialogComponent } from '../delete/sensitive-data-delete-dialog.component';

@Component({
  selector: 'jhi-sensitive-data',
  templateUrl: './sensitive-data.component.html',
})
export class SensitiveDataComponent implements OnInit {
  sensitiveData?: ISensitiveData[];
  isLoading = false;

  constructor(protected sensitiveDataService: SensitiveDataService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.sensitiveDataService.query().subscribe({
      next: (res: HttpResponse<ISensitiveData[]>) => {
        this.isLoading = false;
        this.sensitiveData = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ISensitiveData): number {
    return item.id!;
  }

  delete(sensitiveData: ISensitiveData): void {
    const modalRef = this.modalService.open(SensitiveDataDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sensitiveData = sensitiveData;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
