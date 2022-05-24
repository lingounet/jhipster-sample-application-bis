import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IApplicationType, ApplicationType } from '../application-type.model';
import { ApplicationTypeService } from '../service/application-type.service';

@Component({
  selector: 'jhi-application-type-update',
  templateUrl: './application-type-update.component.html',
})
export class ApplicationTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(
    protected applicationTypeService: ApplicationTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicationType }) => {
      this.updateForm(applicationType);
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
    });
  }

  protected createFromForm(): IApplicationType {
    return {
      ...new ApplicationType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
