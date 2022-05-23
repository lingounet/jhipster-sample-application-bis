import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPsat, Psat } from '../psat.model';
import { PsatService } from '../service/psat.service';
import { Status } from 'app/entities/enumerations/status.model';

@Component({
  selector: 'jhi-psat-update',
  templateUrl: './psat-update.component.html',
})
export class PsatUpdateComponent implements OnInit {
  isSaving = false;
  statusValues = Object.keys(Status);

  editForm = this.fb.group({
    id: [],
    amlId: [],
    owner: [],
    status: [],
  });

  constructor(protected psatService: PsatService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ psat }) => {
      this.updateForm(psat);
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
    });
  }

  protected createFromForm(): IPsat {
    return {
      ...new Psat(),
      id: this.editForm.get(['id'])!.value,
      amlId: this.editForm.get(['amlId'])!.value,
      owner: this.editForm.get(['owner'])!.value,
      status: this.editForm.get(['status'])!.value,
    };
  }
}
