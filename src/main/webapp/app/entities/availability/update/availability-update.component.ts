import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAvailability, Availability } from '../availability.model';
import { AvailabilityService } from '../service/availability.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { Criticality } from 'app/entities/enumerations/criticality.model';

@Component({
  selector: 'jhi-availability-update',
  templateUrl: './availability-update.component.html',
})
export class AvailabilityUpdateComponent implements OnInit {
  isSaving = false;
  criticalityValues = Object.keys(Criticality);

  editForm = this.fb.group({
    id: [],
    financial: [],
    legal: [],
    image: [],
    strategy: [],
    operational: [],
    traceability: [],
    traceabilityContentType: [],
    confidentiality: [],
    confidentialityContentType: [],
    integrity: [],
    integrityContentType: [],
    critical: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected availabilityService: AvailabilityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ availability }) => {
      this.updateForm(availability);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('jhipsterSampleApplicationBisApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const availability = this.createFromForm();
    if (availability.id !== undefined) {
      this.subscribeToSaveResponse(this.availabilityService.update(availability));
    } else {
      this.subscribeToSaveResponse(this.availabilityService.create(availability));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAvailability>>): void {
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

  protected updateForm(availability: IAvailability): void {
    this.editForm.patchValue({
      id: availability.id,
      financial: availability.financial,
      legal: availability.legal,
      image: availability.image,
      strategy: availability.strategy,
      operational: availability.operational,
      traceability: availability.traceability,
      traceabilityContentType: availability.traceabilityContentType,
      confidentiality: availability.confidentiality,
      confidentialityContentType: availability.confidentialityContentType,
      integrity: availability.integrity,
      integrityContentType: availability.integrityContentType,
      critical: availability.critical,
    });
  }

  protected createFromForm(): IAvailability {
    return {
      ...new Availability(),
      id: this.editForm.get(['id'])!.value,
      financial: this.editForm.get(['financial'])!.value,
      legal: this.editForm.get(['legal'])!.value,
      image: this.editForm.get(['image'])!.value,
      strategy: this.editForm.get(['strategy'])!.value,
      operational: this.editForm.get(['operational'])!.value,
      traceabilityContentType: this.editForm.get(['traceabilityContentType'])!.value,
      traceability: this.editForm.get(['traceability'])!.value,
      confidentialityContentType: this.editForm.get(['confidentialityContentType'])!.value,
      confidentiality: this.editForm.get(['confidentiality'])!.value,
      integrityContentType: this.editForm.get(['integrityContentType'])!.value,
      integrity: this.editForm.get(['integrity'])!.value,
      critical: this.editForm.get(['critical'])!.value,
    };
  }
}
