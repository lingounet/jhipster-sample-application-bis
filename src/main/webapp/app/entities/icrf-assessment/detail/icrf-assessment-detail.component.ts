import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIcrfAssessment } from '../icrf-assessment.model';

@Component({
  selector: 'jhi-icrf-assessment-detail',
  templateUrl: './icrf-assessment-detail.component.html',
})
export class IcrfAssessmentDetailComponent implements OnInit {
  icrfAssessment: IIcrfAssessment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ icrfAssessment }) => {
      this.icrfAssessment = icrfAssessment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
