import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPersonalDataType, PersonalDataType } from '../personal-data-type.model';
import { PersonalDataTypeService } from '../service/personal-data-type.service';
import { IPersonalDataRegion } from 'app/entities/personal-data-region/personal-data-region.model';
import { PersonalDataRegionService } from 'app/entities/personal-data-region/service/personal-data-region.service';

@Component({
  selector: 'jhi-personal-data-type-update',
  templateUrl: './personal-data-type-update.component.html',
})
export class PersonalDataTypeUpdateComponent implements OnInit {
  isSaving = false;

  personalDataRegionsSharedCollection: IPersonalDataRegion[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    personalDataRegion: [],
  });

  constructor(
    protected personalDataTypeService: PersonalDataTypeService,
    protected personalDataRegionService: PersonalDataRegionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personalDataType }) => {
      this.updateForm(personalDataType);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personalDataType = this.createFromForm();
    if (personalDataType.id !== undefined) {
      this.subscribeToSaveResponse(this.personalDataTypeService.update(personalDataType));
    } else {
      this.subscribeToSaveResponse(this.personalDataTypeService.create(personalDataType));
    }
  }

  trackPersonalDataRegionById(_index: number, item: IPersonalDataRegion): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonalDataType>>): void {
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

  protected updateForm(personalDataType: IPersonalDataType): void {
    this.editForm.patchValue({
      id: personalDataType.id,
      name: personalDataType.name,
      personalDataRegion: personalDataType.personalDataRegion,
    });

    this.personalDataRegionsSharedCollection = this.personalDataRegionService.addPersonalDataRegionToCollectionIfMissing(
      this.personalDataRegionsSharedCollection,
      personalDataType.personalDataRegion
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personalDataRegionService
      .query()
      .pipe(map((res: HttpResponse<IPersonalDataRegion[]>) => res.body ?? []))
      .pipe(
        map((personalDataRegions: IPersonalDataRegion[]) =>
          this.personalDataRegionService.addPersonalDataRegionToCollectionIfMissing(
            personalDataRegions,
            this.editForm.get('personalDataRegion')!.value
          )
        )
      )
      .subscribe((personalDataRegions: IPersonalDataRegion[]) => (this.personalDataRegionsSharedCollection = personalDataRegions));
  }

  protected createFromForm(): IPersonalDataType {
    return {
      ...new PersonalDataType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      personalDataRegion: this.editForm.get(['personalDataRegion'])!.value,
    };
  }
}
