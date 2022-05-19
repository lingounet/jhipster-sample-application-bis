import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAml } from '../aml.model';
import { AmlService } from '../service/aml.service';
import { AmlDeleteDialogComponent } from '../delete/aml-delete-dialog.component';

@Component({
  selector: 'jhi-aml',
  templateUrl: './aml.component.html',
})
export class AmlComponent implements OnInit {
  amls?: IAml[];
  isLoading = false;

  constructor(protected amlService: AmlService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.amlService.query().subscribe({
      next: (res: HttpResponse<IAml[]>) => {
        this.isLoading = false;
        this.amls = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IAml): number {
    return item.id!;
  }

  delete(aml: IAml): void {
    const modalRef = this.modalService.open(AmlDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.aml = aml;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
