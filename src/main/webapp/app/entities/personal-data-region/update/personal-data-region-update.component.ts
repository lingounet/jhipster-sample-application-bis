import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPersonalDataRegion, PersonalDataRegion } from '../personal-data-region.model';
import { PersonalDataRegionService } from '../service/personal-data-region.service';

@Component({
  selector: 'jhi-personal-data-region-update',
  templateUrl: './personal-data-region-update.component.html',
})
export class PersonalDataRegionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(
    protected personalDataRegionService: PersonalDataRegionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personalDataRegion }) => {
      this.updateForm(personalDataRegion);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personalDataRegion = this.createFromForm();
    if (personalDataRegion.id !== undefined) {
      this.subscribeToSaveResponse(this.personalDataRegionService.update(personalDataRegion));
    } else {
      this.subscribeToSaveResponse(this.personalDataRegionService.create(personalDataRegion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonalDataRegion>>): void {
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

  protected updateForm(personalDataRegion: IPersonalDataRegion): void {
    this.editForm.patchValue({
      id: personalDataRegion.id,
      name: personalDataRegion.name,
    });
  }

  protected createFromForm(): IPersonalDataRegion {
    return {
      ...new PersonalDataRegion(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
