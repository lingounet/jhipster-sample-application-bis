import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IIcrfStatus, IcrfStatus } from '../icrf-status.model';
import { IcrfStatusService } from '../service/icrf-status.service';

@Component({
  selector: 'jhi-icrf-status-update',
  templateUrl: './icrf-status-update.component.html',
})
export class IcrfStatusUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(protected icrfStatusService: IcrfStatusService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ icrfStatus }) => {
      this.updateForm(icrfStatus);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const icrfStatus = this.createFromForm();
    if (icrfStatus.id !== undefined) {
      this.subscribeToSaveResponse(this.icrfStatusService.update(icrfStatus));
    } else {
      this.subscribeToSaveResponse(this.icrfStatusService.create(icrfStatus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIcrfStatus>>): void {
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

  protected updateForm(icrfStatus: IIcrfStatus): void {
    this.editForm.patchValue({
      id: icrfStatus.id,
      name: icrfStatus.name,
    });
  }

  protected createFromForm(): IIcrfStatus {
    return {
      ...new IcrfStatus(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
