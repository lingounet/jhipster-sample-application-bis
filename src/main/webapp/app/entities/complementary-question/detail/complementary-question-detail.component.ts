import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IComplementaryQuestion } from '../complementary-question.model';

@Component({
  selector: 'jhi-complementary-question-detail',
  templateUrl: './complementary-question-detail.component.html',
})
export class ComplementaryQuestionDetailComponent implements OnInit {
  complementaryQuestion: IComplementaryQuestion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ complementaryQuestion }) => {
      this.complementaryQuestion = complementaryQuestion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
