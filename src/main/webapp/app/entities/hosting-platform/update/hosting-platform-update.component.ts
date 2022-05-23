import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IHostingPlatform, HostingPlatform } from '../hosting-platform.model';
import { HostingPlatformService } from '../service/hosting-platform.service';
import { IHostingType } from 'app/entities/hosting-type/hosting-type.model';
import { HostingTypeService } from 'app/entities/hosting-type/service/hosting-type.service';

@Component({
  selector: 'jhi-hosting-platform-update',
  templateUrl: './hosting-platform-update.component.html',
})
export class HostingPlatformUpdateComponent implements OnInit {
  isSaving = false;

  hostingTypesSharedCollection: IHostingType[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    hostingType: [],
  });

  constructor(
    protected hostingPlatformService: HostingPlatformService,
    protected hostingTypeService: HostingTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hostingPlatform }) => {
      this.updateForm(hostingPlatform);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hostingPlatform = this.createFromForm();
    if (hostingPlatform.id !== undefined) {
      this.subscribeToSaveResponse(this.hostingPlatformService.update(hostingPlatform));
    } else {
      this.subscribeToSaveResponse(this.hostingPlatformService.create(hostingPlatform));
    }
  }

  trackHostingTypeById(_index: number, item: IHostingType): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHostingPlatform>>): void {
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

  protected updateForm(hostingPlatform: IHostingPlatform): void {
    this.editForm.patchValue({
      id: hostingPlatform.id,
      name: hostingPlatform.name,
      hostingType: hostingPlatform.hostingType,
    });

    this.hostingTypesSharedCollection = this.hostingTypeService.addHostingTypeToCollectionIfMissing(
      this.hostingTypesSharedCollection,
      hostingPlatform.hostingType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.hostingTypeService
      .query()
      .pipe(map((res: HttpResponse<IHostingType[]>) => res.body ?? []))
      .pipe(
        map((hostingTypes: IHostingType[]) =>
          this.hostingTypeService.addHostingTypeToCollectionIfMissing(hostingTypes, this.editForm.get('hostingType')!.value)
        )
      )
      .subscribe((hostingTypes: IHostingType[]) => (this.hostingTypesSharedCollection = hostingTypes));
  }

  protected createFromForm(): IHostingPlatform {
    return {
      ...new HostingPlatform(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      hostingType: this.editForm.get(['hostingType'])!.value,
    };
  }
}
