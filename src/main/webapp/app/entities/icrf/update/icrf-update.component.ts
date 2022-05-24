import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IIcrf, Icrf } from '../icrf.model';
import { IcrfService } from '../service/icrf.service';
import { IIcrfAssessment } from 'app/entities/icrf-assessment/icrf-assessment.model';
import { IcrfAssessmentService } from 'app/entities/icrf-assessment/service/icrf-assessment.service';
import { IIdentity } from 'app/entities/identity/identity.model';
import { IdentityService } from 'app/entities/identity/service/identity.service';

@Component({
  selector: 'jhi-icrf-update',
  templateUrl: './icrf-update.component.html',
})
export class IcrfUpdateComponent implements OnInit {
  isSaving = false;

  icrfAssessmentsSharedCollection: IIcrfAssessment[] = [];
  identitiesSharedCollection: IIdentity[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    icrfAssessment: [],
    identity: [],
  });

  constructor(
    protected icrfService: IcrfService,
    protected icrfAssessmentService: IcrfAssessmentService,
    protected identityService: IdentityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ icrf }) => {
      if (icrf.id === undefined) {
        const today = dayjs().startOf('day');
        icrf.date = today;
      }

      this.updateForm(icrf);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const icrf = this.createFromForm();
    if (icrf.id !== undefined) {
      this.subscribeToSaveResponse(this.icrfService.update(icrf));
    } else {
      this.subscribeToSaveResponse(this.icrfService.create(icrf));
    }
  }

  trackIcrfAssessmentById(_index: number, item: IIcrfAssessment): number {
    return item.id!;
  }

  trackIdentityById(_index: number, item: IIdentity): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIcrf>>): void {
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

  protected updateForm(icrf: IIcrf): void {
    this.editForm.patchValue({
      id: icrf.id,
      date: icrf.date ? icrf.date.format(DATE_TIME_FORMAT) : null,
      icrfAssessment: icrf.icrfAssessment,
      identity: icrf.identity,
    });

    this.icrfAssessmentsSharedCollection = this.icrfAssessmentService.addIcrfAssessmentToCollectionIfMissing(
      this.icrfAssessmentsSharedCollection,
      icrf.icrfAssessment
    );
    this.identitiesSharedCollection = this.identityService.addIdentityToCollectionIfMissing(this.identitiesSharedCollection, icrf.identity);
  }

  protected loadRelationshipsOptions(): void {
    this.icrfAssessmentService
      .query()
      .pipe(map((res: HttpResponse<IIcrfAssessment[]>) => res.body ?? []))
      .pipe(
        map((icrfAssessments: IIcrfAssessment[]) =>
          this.icrfAssessmentService.addIcrfAssessmentToCollectionIfMissing(icrfAssessments, this.editForm.get('icrfAssessment')!.value)
        )
      )
      .subscribe((icrfAssessments: IIcrfAssessment[]) => (this.icrfAssessmentsSharedCollection = icrfAssessments));

    this.identityService
      .query()
      .pipe(map((res: HttpResponse<IIdentity[]>) => res.body ?? []))
      .pipe(
        map((identities: IIdentity[]) =>
          this.identityService.addIdentityToCollectionIfMissing(identities, this.editForm.get('identity')!.value)
        )
      )
      .subscribe((identities: IIdentity[]) => (this.identitiesSharedCollection = identities));
  }

  protected createFromForm(): IIcrf {
    return {
      ...new Icrf(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      icrfAssessment: this.editForm.get(['icrfAssessment'])!.value,
      identity: this.editForm.get(['identity'])!.value,
    };
  }
}
