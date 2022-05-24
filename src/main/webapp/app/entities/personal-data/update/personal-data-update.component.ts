import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPersonalData, PersonalData } from '../personal-data.model';
import { PersonalDataService } from '../service/personal-data.service';
import { IPersonalDataType } from 'app/entities/personal-data-type/personal-data-type.model';
import { PersonalDataTypeService } from 'app/entities/personal-data-type/service/personal-data-type.service';
import { IIdentity } from 'app/entities/identity/identity.model';
import { IdentityService } from 'app/entities/identity/service/identity.service';

@Component({
  selector: 'jhi-personal-data-update',
  templateUrl: './personal-data-update.component.html',
})
export class PersonalDataUpdateComponent implements OnInit {
  isSaving = false;

  personalDataTypesSharedCollection: IPersonalDataType[] = [];
  identitiesSharedCollection: IIdentity[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    personalDataType: [],
    identity: [],
  });

  constructor(
    protected personalDataService: PersonalDataService,
    protected personalDataTypeService: PersonalDataTypeService,
    protected identityService: IdentityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personalData }) => {
      if (personalData.id === undefined) {
        const today = dayjs().startOf('day');
        personalData.date = today;
      }

      this.updateForm(personalData);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personalData = this.createFromForm();
    if (personalData.id !== undefined) {
      this.subscribeToSaveResponse(this.personalDataService.update(personalData));
    } else {
      this.subscribeToSaveResponse(this.personalDataService.create(personalData));
    }
  }

  trackPersonalDataTypeById(_index: number, item: IPersonalDataType): number {
    return item.id!;
  }

  trackIdentityById(_index: number, item: IIdentity): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonalData>>): void {
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

  protected updateForm(personalData: IPersonalData): void {
    this.editForm.patchValue({
      id: personalData.id,
      date: personalData.date ? personalData.date.format(DATE_TIME_FORMAT) : null,
      personalDataType: personalData.personalDataType,
      identity: personalData.identity,
    });

    this.personalDataTypesSharedCollection = this.personalDataTypeService.addPersonalDataTypeToCollectionIfMissing(
      this.personalDataTypesSharedCollection,
      personalData.personalDataType
    );
    this.identitiesSharedCollection = this.identityService.addIdentityToCollectionIfMissing(
      this.identitiesSharedCollection,
      personalData.identity
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personalDataTypeService
      .query()
      .pipe(map((res: HttpResponse<IPersonalDataType[]>) => res.body ?? []))
      .pipe(
        map((personalDataTypes: IPersonalDataType[]) =>
          this.personalDataTypeService.addPersonalDataTypeToCollectionIfMissing(
            personalDataTypes,
            this.editForm.get('personalDataType')!.value
          )
        )
      )
      .subscribe((personalDataTypes: IPersonalDataType[]) => (this.personalDataTypesSharedCollection = personalDataTypes));

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

  protected createFromForm(): IPersonalData {
    return {
      ...new PersonalData(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      personalDataType: this.editForm.get(['personalDataType'])!.value,
      identity: this.editForm.get(['identity'])!.value,
    };
  }
}
