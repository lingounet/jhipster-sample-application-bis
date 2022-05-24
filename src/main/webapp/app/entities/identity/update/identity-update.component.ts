import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IIdentity, Identity } from '../identity.model';
import { IdentityService } from '../service/identity.service';
import { IPsat } from 'app/entities/psat/psat.model';
import { PsatService } from 'app/entities/psat/service/psat.service';
import { IAvailability } from 'app/entities/availability/availability.model';
import { AvailabilityService } from 'app/entities/availability/service/availability.service';
import { IApplicationType } from 'app/entities/application-type/application-type.model';
import { ApplicationTypeService } from 'app/entities/application-type/service/application-type.service';
import { Process } from 'app/entities/enumerations/process.model';

@Component({
  selector: 'jhi-identity-update',
  templateUrl: './identity-update.component.html',
})
export class IdentityUpdateComponent implements OnInit {
  isSaving = false;
  processValues = Object.keys(Process);

  psatsCollection: IPsat[] = [];
  availabilitiesCollection: IAvailability[] = [];
  applicationTypesSharedCollection: IApplicationType[] = [];

  editForm = this.fb.group({
    id: [],
    applicationName: [null, [Validators.required]],
    so: [null, [Validators.required]],
    process: [],
    psat: [],
    availability: [],
    applicationType: [],
  });

  constructor(
    protected identityService: IdentityService,
    protected psatService: PsatService,
    protected availabilityService: AvailabilityService,
    protected applicationTypeService: ApplicationTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ identity }) => {
      this.updateForm(identity);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const identity = this.createFromForm();
    if (identity.id !== undefined) {
      this.subscribeToSaveResponse(this.identityService.update(identity));
    } else {
      this.subscribeToSaveResponse(this.identityService.create(identity));
    }
  }

  trackPsatById(_index: number, item: IPsat): number {
    return item.id!;
  }

  trackAvailabilityById(_index: number, item: IAvailability): number {
    return item.id!;
  }

  trackApplicationTypeById(_index: number, item: IApplicationType): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIdentity>>): void {
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

  protected updateForm(identity: IIdentity): void {
    this.editForm.patchValue({
      id: identity.id,
      applicationName: identity.applicationName,
      so: identity.so,
      process: identity.process,
      psat: identity.psat,
      availability: identity.availability,
      applicationType: identity.applicationType,
    });

    this.psatsCollection = this.psatService.addPsatToCollectionIfMissing(this.psatsCollection, identity.psat);
    this.availabilitiesCollection = this.availabilityService.addAvailabilityToCollectionIfMissing(
      this.availabilitiesCollection,
      identity.availability
    );
    this.applicationTypesSharedCollection = this.applicationTypeService.addApplicationTypeToCollectionIfMissing(
      this.applicationTypesSharedCollection,
      identity.applicationType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.psatService
      .query({ filter: 'identity-is-null' })
      .pipe(map((res: HttpResponse<IPsat[]>) => res.body ?? []))
      .pipe(map((psats: IPsat[]) => this.psatService.addPsatToCollectionIfMissing(psats, this.editForm.get('psat')!.value)))
      .subscribe((psats: IPsat[]) => (this.psatsCollection = psats));

    this.availabilityService
      .query({ filter: 'identity-is-null' })
      .pipe(map((res: HttpResponse<IAvailability[]>) => res.body ?? []))
      .pipe(
        map((availabilities: IAvailability[]) =>
          this.availabilityService.addAvailabilityToCollectionIfMissing(availabilities, this.editForm.get('availability')!.value)
        )
      )
      .subscribe((availabilities: IAvailability[]) => (this.availabilitiesCollection = availabilities));

    this.applicationTypeService
      .query()
      .pipe(map((res: HttpResponse<IApplicationType[]>) => res.body ?? []))
      .pipe(
        map((applicationTypes: IApplicationType[]) =>
          this.applicationTypeService.addApplicationTypeToCollectionIfMissing(applicationTypes, this.editForm.get('applicationType')!.value)
        )
      )
      .subscribe((applicationTypes: IApplicationType[]) => (this.applicationTypesSharedCollection = applicationTypes));
  }

  protected createFromForm(): IIdentity {
    return {
      ...new Identity(),
      id: this.editForm.get(['id'])!.value,
      applicationName: this.editForm.get(['applicationName'])!.value,
      so: this.editForm.get(['so'])!.value,
      process: this.editForm.get(['process'])!.value,
      psat: this.editForm.get(['psat'])!.value,
      availability: this.editForm.get(['availability'])!.value,
      applicationType: this.editForm.get(['applicationType'])!.value,
    };
  }
}
