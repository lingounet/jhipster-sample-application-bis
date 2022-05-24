import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IHosting, Hosting } from '../hosting.model';
import { HostingService } from '../service/hosting.service';
import { IHostingPlatform } from 'app/entities/hosting-platform/hosting-platform.model';
import { HostingPlatformService } from 'app/entities/hosting-platform/service/hosting-platform.service';
import { ISecurityInterview } from 'app/entities/security-interview/security-interview.model';
import { SecurityInterviewService } from 'app/entities/security-interview/service/security-interview.service';

@Component({
  selector: 'jhi-hosting-update',
  templateUrl: './hosting-update.component.html',
})
export class HostingUpdateComponent implements OnInit {
  isSaving = false;

  hostingPlatformsSharedCollection: IHostingPlatform[] = [];
  securityInterviewsSharedCollection: ISecurityInterview[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    hostingPlaform: [],
    securityInterview: [],
  });

  constructor(
    protected hostingService: HostingService,
    protected hostingPlatformService: HostingPlatformService,
    protected securityInterviewService: SecurityInterviewService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hosting }) => {
      if (hosting.id === undefined) {
        const today = dayjs().startOf('day');
        hosting.date = today;
      }

      this.updateForm(hosting);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hosting = this.createFromForm();
    if (hosting.id !== undefined) {
      this.subscribeToSaveResponse(this.hostingService.update(hosting));
    } else {
      this.subscribeToSaveResponse(this.hostingService.create(hosting));
    }
  }

  trackHostingPlatformById(_index: number, item: IHostingPlatform): number {
    return item.id!;
  }

  trackSecurityInterviewById(_index: number, item: ISecurityInterview): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHosting>>): void {
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

  protected updateForm(hosting: IHosting): void {
    this.editForm.patchValue({
      id: hosting.id,
      date: hosting.date ? hosting.date.format(DATE_TIME_FORMAT) : null,
      hostingPlaform: hosting.hostingPlaform,
      securityInterview: hosting.securityInterview,
    });

    this.hostingPlatformsSharedCollection = this.hostingPlatformService.addHostingPlatformToCollectionIfMissing(
      this.hostingPlatformsSharedCollection,
      hosting.hostingPlaform
    );
    this.securityInterviewsSharedCollection = this.securityInterviewService.addSecurityInterviewToCollectionIfMissing(
      this.securityInterviewsSharedCollection,
      hosting.securityInterview
    );
  }

  protected loadRelationshipsOptions(): void {
    this.hostingPlatformService
      .query()
      .pipe(map((res: HttpResponse<IHostingPlatform[]>) => res.body ?? []))
      .pipe(
        map((hostingPlatforms: IHostingPlatform[]) =>
          this.hostingPlatformService.addHostingPlatformToCollectionIfMissing(hostingPlatforms, this.editForm.get('hostingPlaform')!.value)
        )
      )
      .subscribe((hostingPlatforms: IHostingPlatform[]) => (this.hostingPlatformsSharedCollection = hostingPlatforms));

    this.securityInterviewService
      .query()
      .pipe(map((res: HttpResponse<ISecurityInterview[]>) => res.body ?? []))
      .pipe(
        map((securityInterviews: ISecurityInterview[]) =>
          this.securityInterviewService.addSecurityInterviewToCollectionIfMissing(
            securityInterviews,
            this.editForm.get('securityInterview')!.value
          )
        )
      )
      .subscribe((securityInterviews: ISecurityInterview[]) => (this.securityInterviewsSharedCollection = securityInterviews));
  }

  protected createFromForm(): IHosting {
    return {
      ...new Hosting(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      hostingPlaform: this.editForm.get(['hostingPlaform'])!.value,
      securityInterview: this.editForm.get(['securityInterview'])!.value,
    };
  }
}
