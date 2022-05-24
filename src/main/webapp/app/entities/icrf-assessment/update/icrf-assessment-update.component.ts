import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IIcrfAssessment, IcrfAssessment } from '../icrf-assessment.model';
import { IcrfAssessmentService } from '../service/icrf-assessment.service';

@Component({
  selector: 'jhi-icrf-assessment-update',
  templateUrl: './icrf-assessment-update.component.html',
})
export class IcrfAssessmentUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    code: [],
    description: [],
    status: [],
  });

  constructor(
    protected icrfAssessmentService: IcrfAssessmentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ icrfAssessment }) => {
      this.updateForm(icrfAssessment);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const icrfAssessment = this.createFromForm();
    if (icrfAssessment.id !== undefined) {
      this.subscribeToSaveResponse(this.icrfAssessmentService.update(icrfAssessment));
    } else {
      this.subscribeToSaveResponse(this.icrfAssessmentService.create(icrfAssessment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIcrfAssessment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(icrfAssessment: IIcrfAssessment): void {
    this.editForm.patchValue({
      id: icrfAssessment.id,
      code: icrfAssessment.code,
      description: icrfAssessment.description,
      status: icrfAssessment.status,
    });
  }

  protected createFromForm(): IIcrfAssessment {
    return {
      ...new IcrfAssessment(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      description: this.editForm.get(['description'])!.value,
      status: this.editForm.get(['status'])!.value,
    };
  }
}
