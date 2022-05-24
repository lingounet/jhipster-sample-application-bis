import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonalDataRegion } from '../personal-data-region.model';
import { PersonalDataRegionService } from '../service/personal-data-region.service';
import { PersonalDataRegionDeleteDialogComponent } from '../delete/personal-data-region-delete-dialog.component';

@Component({
  selector: 'jhi-personal-data-region',
  templateUrl: './personal-data-region.component.html',
})
export class PersonalDataRegionComponent implements OnInit {
  personalDataRegions?: IPersonalDataRegion[];
  isLoading = false;

  constructor(protected personalDataRegionService: PersonalDataRegionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.personalDataRegionService.query().subscribe({
      next: (res: HttpResponse<IPersonalDataRegion[]>) => {
        this.isLoading = false;
        this.personalDataRegions = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPersonalDataRegion): number {
    return item.id!;
  }

  delete(personalDataRegion: IPersonalDataRegion): void {
    const modalRef = this.modalService.open(PersonalDataRegionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.personalDataRegion = personalDataRegion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
