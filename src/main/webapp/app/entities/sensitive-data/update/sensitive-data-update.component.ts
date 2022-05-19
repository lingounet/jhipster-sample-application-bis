import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISensitiveData, SensitiveData } from '../sensitive-data.model';
import { SensitiveDataService } from '../service/sensitive-data.service';
import { ISensitiveDataType } from 'app/entities/sensitive-data-type/sensitive-data-type.model';
import { SensitiveDataTypeService } from 'app/entities/sensitive-data-type/service/sensitive-data-type.service';
import { ISecurityInterview } from 'app/entities/security-interview/security-interview.model';
import { SecurityInterviewService } from 'app/entities/security-interview/service/security-interview.service';

@Component({
  selector: 'jhi-sensitive-data-update',
  templateUrl: './sensitive-data-update.component.html',
})
export class SensitiveDataUpdateComponent implements OnInit {
  isSaving = false;

  sensitiveDataTypesSharedCollection: ISensitiveDataType[] = [];
  securityInterviewsSharedCollection: ISecurityInterview[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    sensitiveDataType: [],
    securityInterview: [],
  });

  constructor(
    protected sensitiveDataService: SensitiveDataService,
    protected sensitiveDataTypeService: SensitiveDataTypeService,
    protected securityInterviewService: SecurityInterviewService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sensitiveData }) => {
      if (sensitiveData.id === undefined) {
        const today = dayjs().startOf('day');
        sensitiveData.date = today;
      }

      this.updateForm(sensitiveData);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sensitiveData = this.createFromForm();
    if (sensitiveData.id !== undefined) {
      this.subscribeToSaveResponse(this.sensitiveDataService.update(sensitiveData));
    } else {
      this.subscribeToSaveResponse(this.sensitiveDataService.create(sensitiveData));
    }
  }

  trackSensitiveDataTypeById(_index: number, item: ISensitiveDataType): number {
    return item.id!;
  }

  trackSecurityInterviewById(_index: number, item: ISecurityInterview): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISensitiveData>>): void {
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

  protected updateForm(sensitiveData: ISensitiveData): void {
    this.editForm.patchValue({
      id: sensitiveData.id,
      date: sensitiveData.date ? sensitiveData.date.format(DATE_TIME_FORMAT) : null,
      sensitiveDataType: sensitiveData.sensitiveDataType,
      securityInterview: sensitiveData.securityInterview,
    });

    this.sensitiveDataTypesSharedCollection = this.sensitiveDataTypeService.addSensitiveDataTypeToCollectionIfMissing(
      this.sensitiveDataTypesSharedCollection,
      sensitiveData.sensitiveDataType
    );
    this.securityInterviewsSharedCollection = this.securityInterviewService.addSecurityInterviewToCollectionIfMissing(
      this.securityInterviewsSharedCollection,
      sensitiveData.securityInterview
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sensitiveDataTypeService
      .query()
      .pipe(map((res: HttpResponse<ISensitiveDataType[]>) => res.body ?? []))
      .pipe(
        map((sensitiveDataTypes: ISensitiveDataType[]) =>
          this.sensitiveDataTypeService.addSensitiveDataTypeToCollectionIfMissing(
            sensitiveDataTypes,
            this.editForm.get('sensitiveDataType')!.value
          )
        )
      )
      .subscribe((sensitiveDataTypes: ISensitiveDataType[]) => (this.sensitiveDataTypesSharedCollection = sensitiveDataTypes));

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

  protected createFromForm(): ISensitiveData {
    return {
      ...new SensitiveData(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      sensitiveDataType: this.editForm.get(['sensitiveDataType'])!.value,
      securityInterview: this.editForm.get(['securityInterview'])!.value,
    };
  }
}
