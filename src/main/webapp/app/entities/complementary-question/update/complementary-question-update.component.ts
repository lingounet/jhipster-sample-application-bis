import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IComplementaryQuestion, ComplementaryQuestion } from '../complementary-question.model';
import { ComplementaryQuestionService } from '../service/complementary-question.service';
import { ISecurityInterview } from 'app/entities/security-interview/security-interview.model';
import { SecurityInterviewService } from 'app/entities/security-interview/service/security-interview.service';

@Component({
  selector: 'jhi-complementary-question-update',
  templateUrl: './complementary-question-update.component.html',
})
export class ComplementaryQuestionUpdateComponent implements OnInit {
  isSaving = false;

  securityInterviewsSharedCollection: ISecurityInterview[] = [];

  editForm = this.fb.group({
    id: [],
    internet: [],
    development: [],
    configuration: [],
    cloud: [],
    internal: [],
    partner: [],
    users: [],
    securityInterview: [],
  });

  constructor(
    protected complementaryQuestionService: ComplementaryQuestionService,
    protected securityInterviewService: SecurityInterviewService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ complementaryQuestion }) => {
      this.updateForm(complementaryQuestion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const complementaryQuestion = this.createFromForm();
    if (complementaryQuestion.id !== undefined) {
      this.subscribeToSaveResponse(this.complementaryQuestionService.update(complementaryQuestion));
    } else {
      this.subscribeToSaveResponse(this.complementaryQuestionService.create(complementaryQuestion));
    }
  }

  trackSecurityInterviewById(_index: number, item: ISecurityInterview): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComplementaryQuestion>>): void {
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

  protected updateForm(complementaryQuestion: IComplementaryQuestion): void {
    this.editForm.patchValue({
      id: complementaryQuestion.id,
      internet: complementaryQuestion.internet,
      development: complementaryQuestion.development,
      configuration: complementaryQuestion.configuration,
      cloud: complementaryQuestion.cloud,
      internal: complementaryQuestion.internal,
      partner: complementaryQuestion.partner,
      users: complementaryQuestion.users,
      securityInterview: complementaryQuestion.securityInterview,
    });

    this.securityInterviewsSharedCollection = this.securityInterviewService.addSecurityInterviewToCollectionIfMissing(
      this.securityInterviewsSharedCollection,
      complementaryQuestion.securityInterview
    );
  }

  protected loadRelationshipsOptions(): void {
    this.securityInterviewService
      .query()
      .pipe(map((res: HttpResponse<ISecurityInterview[]>) => res.body ?? []))
      .pipe(
        map((securityInterviews: ISecurityInterview[]) =>
          this.securityInterviewService.addSecurityInterviewToCollectionIfMissing(
            securityInterviews,
            this.editForm.get('securityInterview')!.value
          )
        )
      )
      .subscribe((securityInterviews: ISecurityInterview[]) => (this.securityInterviewsSharedCollection = securityInterviews));
  }

  protected createFromForm(): IComplementaryQuestion {
    return {
      ...new ComplementaryQuestion(),
      id: this.editForm.get(['id'])!.value,
      internet: this.editForm.get(['internet'])!.value,
      development: this.editForm.get(['development'])!.value,
      configuration: this.editForm.get(['configuration'])!.value,
      cloud: this.editForm.get(['cloud'])!.value,
      internal: this.editForm.get(['internal'])!.value,
      partner: this.editForm.get(['partner'])!.value,
      users: this.editForm.get(['users'])!.value,
      securityInterview: this.editForm.get(['securityInterview'])!.value,
    };
  }
}
