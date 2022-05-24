import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IIcrfAssessment } from '../icrf-assessment.model';
import { IcrfAssessmentService } from '../service/icrf-assessment.service';
import { IcrfAssessmentDeleteDialogComponent } from '../delete/icrf-assessment-delete-dialog.component';

@Component({
  selector: 'jhi-icrf-assessment',
  templateUrl: './icrf-assessment.component.html',
})
export class IcrfAssessmentComponent implements OnInit {
  icrfAssessments?: IIcrfAssessment[];
  isLoading = false;

  constructor(protected icrfAssessmentService: IcrfAssessmentService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.icrfAssessmentService.query().subscribe({
      next: (res: HttpResponse<IIcrfAssessment[]>) => {
        this.isLoading = false;
        this.icrfAssessments = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IIcrfAssessment): number {
    return item.id!;
  }

  delete(icrfAssessment: IIcrfAssessment): void {
    const modalRef = this.modalService.open(IcrfAssessmentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.icrfAssessment = icrfAssessment;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
