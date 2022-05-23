import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISecurityInterview, SecurityInterview } from '../security-interview.model';
import { SecurityInterviewService } from '../service/security-interview.service';
import { IPsat } from 'app/entities/psat/psat.model';
import { PsatService } from 'app/entities/psat/service/psat.service';
import { IAvailability } from 'app/entities/availability/availability.model';
import { AvailabilityService } from 'app/entities/availability/service/availability.service';
import { Process } from 'app/entities/enumerations/process.model';

@Component({
  selector: 'jhi-security-interview-update',
  templateUrl: './security-interview-update.component.html',
})
export class SecurityInterviewUpdateComponent implements OnInit {
  isSaving = false;
  processValues = Object.keys(Process);

  psatsCollection: IPsat[] = [];
  availabilitiesCollection: IAvailability[] = [];

  editForm = this.fb.group({
    id: [],
    applicationName: [],
    so: [],
    process: [],
    psat: [],
    availability: [],
  });

  constructor(
    protected securityInterviewService: SecurityInterviewService,
    protected psatService: PsatService,
    protected availabilityService: AvailabilityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ securityInterview }) => {
      this.updateForm(securityInterview);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const securityInterview = this.createFromForm();
    if (securityInterview.id !== undefined) {
      this.subscribeToSaveResponse(this.securityInterviewService.update(securityInterview));
    } else {
      this.subscribeToSaveResponse(this.securityInterviewService.create(securityInterview));
    }
  }

  trackPsatById(_index: number, item: IPsat): number {
    return item.id!;
  }

  trackAvailabilityById(_index: number, item: IAvailability): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISecurityInterview>>): void {
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

  protected updateForm(securityInterview: ISecurityInterview): void {
    this.editForm.patchValue({
      id: securityInterview.id,
      applicationName: securityInterview.applicationName,
      so: securityInterview.so,
      process: securityInterview.process,
      psat: securityInterview.psat,
      availability: securityInterview.availability,
    });

    this.psatsCollection = this.psatService.addPsatToCollectionIfMissing(this.psatsCollection, securityInterview.psat);
    this.availabilitiesCollection = this.availabilityService.addAvailabilityToCollectionIfMissing(
      this.availabilitiesCollection,
      securityInterview.availability
    );
  }

  protected loadRelationshipsOptions(): void {
    this.psatService
      .query({ filter: 'securityinterview-is-null' })
      .pipe(map((res: HttpResponse<IPsat[]>) => res.body ?? []))
      .pipe(map((psats: IPsat[]) => this.psatService.addPsatToCollectionIfMissing(psats, this.editForm.get('psat')!.value)))
      .subscribe((psats: IPsat[]) => (this.psatsCollection = psats));

    this.availabilityService
      .query({ filter: 'securityinterview-is-null' })
      .pipe(map((res: HttpResponse<IAvailability[]>) => res.body ?? []))
      .pipe(
        map((availabilities: IAvailability[]) =>
          this.availabilityService.addAvailabilityToCollectionIfMissing(availabilities, this.editForm.get('availability')!.value)
        )
      )
      .subscribe((availabilities: IAvailability[]) => (this.availabilitiesCollection = availabilities));
  }

  protected createFromForm(): ISecurityInterview {
    return {
      ...new SecurityInterview(),
      id: this.editForm.get(['id'])!.value,
      applicationName: this.editForm.get(['applicationName'])!.value,
      so: this.editForm.get(['so'])!.value,
      process: this.editForm.get(['process'])!.value,
      psat: this.editForm.get(['psat'])!.value,
      availability: this.editForm.get(['availability'])!.value,
    };
  }
}
