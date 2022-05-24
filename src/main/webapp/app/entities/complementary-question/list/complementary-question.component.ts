import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IComplementaryQuestion } from '../complementary-question.model';
import { ComplementaryQuestionService } from '../service/complementary-question.service';
import { ComplementaryQuestionDeleteDialogComponent } from '../delete/complementary-question-delete-dialog.component';

@Component({
  selector: 'jhi-complementary-question',
  templateUrl: './complementary-question.component.html',
})
export class ComplementaryQuestionComponent implements OnInit {
  complementaryQuestions?: IComplementaryQuestion[];
  isLoading = false;

  constructor(protected complementaryQuestionService: ComplementaryQuestionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.complementaryQuestionService.query().subscribe({
      next: (res: HttpResponse<IComplementaryQuestion[]>) => {
        this.isLoading = false;
        this.complementaryQuestions = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IComplementaryQuestion): number {
    return item.id!;
  }

  delete(complementaryQuestion: IComplementaryQuestion): void {
    const modalRef = this.modalService.open(ComplementaryQuestionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.complementaryQuestion = complementaryQuestion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
