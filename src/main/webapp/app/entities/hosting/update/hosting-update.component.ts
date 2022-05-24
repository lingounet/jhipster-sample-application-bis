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
import { IIdentity } from 'app/entities/identity/identity.model';
import { IdentityService } from 'app/entities/identity/service/identity.service';

@Component({
  selector: 'jhi-hosting-update',
  templateUrl: './hosting-update.component.html',
})
export class HostingUpdateComponent implements OnInit {
  isSaving = false;

  hostingPlatformsSharedCollection: IHostingPlatform[] = [];
  identitiesSharedCollection: IIdentity[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    hostingPlaform: [],
    identity: [],
  });

  constructor(
    protected hostingService: HostingService,
    protected hostingPlatformService: HostingPlatformService,
    protected identityService: IdentityService,
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

  trackIdentityById(_index: number, item: IIdentity): number {
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
      identity: hosting.identity,
    });

    this.hostingPlatformsSharedCollection = this.hostingPlatformService.addHostingPlatformToCollectionIfMissing(
      this.hostingPlatformsSharedCollection,
      hosting.hostingPlaform
    );
    this.identitiesSharedCollection = this.identityService.addIdentityToCollectionIfMissing(
      this.identitiesSharedCollection,
      hosting.identity
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

    this.identityService
      .query()
      .pipe(map((res: HttpResponse<IIdentity[]>) => res.body ?? []))
      .pipe(
        map((identities: IIdentity[]) =>
          this.identityService.addIdentityToCollectionIfMissing(identities, this.editForm.get('identity')!.value)
        )
      )
      .subscribe((identities: IIdentity[]) => (this.identitiesSharedCollection = identities));
  }

  protected createFromForm(): IHosting {
    return {
      ...new Hosting(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      hostingPlaform: this.editForm.get(['hostingPlaform'])!.value,
      identity: this.editForm.get(['identity'])!.value,
    };
  }
}
