import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IApplicationType, ApplicationType } from '../application-type.model';
import { ApplicationTypeService } from '../service/application-type.service';
import { ISecurityInterview } from 'app/entities/security-interview/security-interview.model';
import { SecurityInterviewService } from 'app/entities/security-interview/service/security-interview.service';

@Component({
  selector: 'jhi-application-type-update',
  templateUrl: './application-type-update.component.html',
})
export class ApplicationTypeUpdateComponent implements OnInit {
  isSaving = false;

  securityInterviewsSharedCollection: ISecurityInterview[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    securityInterview: [],
  });

  constructor(
    protected applicationTypeService: ApplicationTypeService,
    protected securityInterviewService: SecurityInterviewService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicationType }) => {
      this.updateForm(applicationType);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const applicationType = this.createFromForm();
    if (applicationType.id !== undefined) {
      this.subscribeToSaveResponse(this.applicationTypeService.update(applicationType));
    } else {
      this.subscribeToSaveResponse(this.applicationTypeService.create(applicationType));
    }
  }

  trackSecurityInterviewById(_index: number, item: ISecurityInterview): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplicationType>>): void {
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

  protected updateForm(applicationType: IApplicationType): void {
    this.editForm.patchValue({
      id: applicationType.id,
      name: applicationType.name,
      securityInterview: applicationType.securityInterview,
    });

    this.securityInterviewsSharedCollection = this.securityInterviewService.addSecurityInterviewToCollectionIfMissing(
      this.securityInterviewsSharedCollection,
      applicationType.securityInterview
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

  protected createFromForm(): IApplicationType {
    return {
      ...new ApplicationType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      securityInterview: this.editForm.get(['securityInterview'])!.value,
    };
  }
}
