import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPsat, Psat } from '../psat.model';
import { PsatService } from '../service/psat.service';
import { ISecurityInterview } from 'app/entities/security-interview/security-interview.model';
import { SecurityInterviewService } from 'app/entities/security-interview/service/security-interview.service';
import { Status } from 'app/entities/enumerations/status.model';

@Component({
  selector: 'jhi-psat-update',
  templateUrl: './psat-update.component.html',
})
export class PsatUpdateComponent implements OnInit {
  isSaving = false;
  statusValues = Object.keys(Status);

  securityInterviewsCollection: ISecurityInterview[] = [];

  editForm = this.fb.group({
    id: [],
    amlId: [],
    owner: [],
    status: [],
    securityInterview: [],
  });

  constructor(
    protected psatService: PsatService,
    protected securityInterviewService: SecurityInterviewService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ psat }) => {
      this.updateForm(psat);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const psat = this.createFromForm();
    if (psat.id !== undefined) {
      this.subscribeToSaveResponse(this.psatService.update(psat));
    } else {
      this.subscribeToSaveResponse(this.psatService.create(psat));
    }
  }

  trackSecurityInterviewById(_index: number, item: ISecurityInterview): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPsat>>): void {
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

  protected updateForm(psat: IPsat): void {
    this.editForm.patchValue({
      id: psat.id,
      amlId: psat.amlId,
      owner: psat.owner,
      status: psat.status,
      securityInterview: psat.securityInterview,
    });

    this.securityInterviewsCollection = this.securityInterviewService.addSecurityInterviewToCollectionIfMissing(
      this.securityInterviewsCollection,
      psat.securityInterview
    );
  }

  protected loadRelationshipsOptions(): void {
    this.securityInterviewService
      .query({ filter: 'psat-is-null' })
      .pipe(map((res: HttpResponse<ISecurityInterview[]>) => res.body ?? []))
      .pipe(
        map((securityInterviews: ISecurityInterview[]) =>
          this.securityInterviewService.addSecurityInterviewToCollectionIfMissing(
            securityInterviews,
            this.editForm.get('securityInterview')!.value
          )
        )
      )
      .subscribe((securityInterviews: ISecurityInterview[]) => (this.securityInterviewsCollection = securityInterviews));
  }

  protected createFromForm(): IPsat {
    return {
      ...new Psat(),
      id: this.editForm.get(['id'])!.value,
      amlId: this.editForm.get(['amlId'])!.value,
      owner: this.editForm.get(['owner'])!.value,
      status: this.editForm.get(['status'])!.value,
      securityInterview: this.editForm.get(['securityInterview'])!.value,
    };
  }
}
