import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IIcrf, Icrf } from '../icrf.model';
import { IcrfService } from '../service/icrf.service';
import { IIcrfStatus } from 'app/entities/icrf-status/icrf-status.model';
import { IcrfStatusService } from 'app/entities/icrf-status/service/icrf-status.service';
import { ISecurityInterview } from 'app/entities/security-interview/security-interview.model';
import { SecurityInterviewService } from 'app/entities/security-interview/service/security-interview.service';

@Component({
  selector: 'jhi-icrf-update',
  templateUrl: './icrf-update.component.html',
})
export class IcrfUpdateComponent implements OnInit {
  isSaving = false;

  icrfStatusesCollection: IIcrfStatus[] = [];
  securityInterviewsSharedCollection: ISecurityInterview[] = [];

  editForm = this.fb.group({
    id: [],
    code: [],
    description: [],
    icrfStatus: [],
    securityInterview: [],
  });

  constructor(
    protected icrfService: IcrfService,
    protected icrfStatusService: IcrfStatusService,
    protected securityInterviewService: SecurityInterviewService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ icrf }) => {
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

  trackIcrfStatusById(_index: number, item: IIcrfStatus): number {
    return item.id!;
  }

  trackSecurityInterviewById(_index: number, item: ISecurityInterview): number {
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
      code: icrf.code,
      description: icrf.description,
      icrfStatus: icrf.icrfStatus,
      securityInterview: icrf.securityInterview,
    });

    this.icrfStatusesCollection = this.icrfStatusService.addIcrfStatusToCollectionIfMissing(this.icrfStatusesCollection, icrf.icrfStatus);
    this.securityInterviewsSharedCollection = this.securityInterviewService.addSecurityInterviewToCollectionIfMissing(
      this.securityInterviewsSharedCollection,
      icrf.securityInterview
    );
  }

  protected loadRelationshipsOptions(): void {
    this.icrfStatusService
      .query({ filter: 'icrf-is-null' })
      .pipe(map((res: HttpResponse<IIcrfStatus[]>) => res.body ?? []))
      .pipe(
        map((icrfStatuses: IIcrfStatus[]) =>
          this.icrfStatusService.addIcrfStatusToCollectionIfMissing(icrfStatuses, this.editForm.get('icrfStatus')!.value)
        )
      )
      .subscribe((icrfStatuses: IIcrfStatus[]) => (this.icrfStatusesCollection = icrfStatuses));

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

  protected createFromForm(): IIcrf {
    return {
      ...new Icrf(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      description: this.editForm.get(['description'])!.value,
      icrfStatus: this.editForm.get(['icrfStatus'])!.value,
      securityInterview: this.editForm.get(['securityInterview'])!.value,
    };
  }
}
