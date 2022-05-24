import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAml, Aml } from '../aml.model';
import { AmlService } from '../service/aml.service';

@Component({
  selector: 'jhi-aml-update',
  templateUrl: './aml-update.component.html',
})
export class AmlUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(protected amlService: AmlService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aml }) => {
      this.updateForm(aml);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aml = this.createFromForm();
    if (aml.id !== undefined) {
      this.subscribeToSaveResponse(this.amlService.update(aml));
    } else {
      this.subscribeToSaveResponse(this.amlService.create(aml));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAml>>): void {
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

  protected updateForm(aml: IAml): void {
    this.editForm.patchValue({
      id: aml.id,
      name: aml.name,
    });
  }

  protected createFromForm(): IAml {
    return {
      ...new Aml(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
