import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISensitiveDataType, SensitiveDataType } from '../sensitive-data-type.model';
import { SensitiveDataTypeService } from '../service/sensitive-data-type.service';

@Component({
  selector: 'jhi-sensitive-data-type-update',
  templateUrl: './sensitive-data-type-update.component.html',
})
export class SensitiveDataTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(
    protected sensitiveDataTypeService: SensitiveDataTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sensitiveDataType }) => {
      this.updateForm(sensitiveDataType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sensitiveDataType = this.createFromForm();
    if (sensitiveDataType.id !== undefined) {
      this.subscribeToSaveResponse(this.sensitiveDataTypeService.update(sensitiveDataType));
    } else {
      this.subscribeToSaveResponse(this.sensitiveDataTypeService.create(sensitiveDataType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISensitiveDataType>>): void {
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

  protected updateForm(sensitiveDataType: ISensitiveDataType): void {
    this.editForm.patchValue({
      id: sensitiveDataType.id,
      name: sensitiveDataType.name,
    });
  }

  protected createFromForm(): ISensitiveDataType {
    return {
      ...new SensitiveDataType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
