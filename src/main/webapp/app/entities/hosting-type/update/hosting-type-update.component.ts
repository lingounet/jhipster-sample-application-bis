import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IHostingType, HostingType } from '../hosting-type.model';
import { HostingTypeService } from '../service/hosting-type.service';

@Component({
  selector: 'jhi-hosting-type-update',
  templateUrl: './hosting-type-update.component.html',
})
export class HostingTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(protected hostingTypeService: HostingTypeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hostingType }) => {
      this.updateForm(hostingType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hostingType = this.createFromForm();
    if (hostingType.id !== undefined) {
      this.subscribeToSaveResponse(this.hostingTypeService.update(hostingType));
    } else {
      this.subscribeToSaveResponse(this.hostingTypeService.create(hostingType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHostingType>>): void {
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

  protected updateForm(hostingType: IHostingType): void {
    this.editForm.patchValue({
      id: hostingType.id,
      name: hostingType.name,
    });
  }

  protected createFromForm(): IHostingType {
    return {
      ...new HostingType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
