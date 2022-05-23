import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonalData } from '../personal-data.model';
import { PersonalDataService } from '../service/personal-data.service';
import { PersonalDataDeleteDialogComponent } from '../delete/personal-data-delete-dialog.component';

@Component({
  selector: 'jhi-personal-data',
  templateUrl: './personal-data.component.html',
})
export class PersonalDataComponent implements OnInit {
  personalData?: IPersonalData[];
  isLoading = false;

  constructor(protected personalDataService: PersonalDataService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.personalDataService.query().subscribe({
      next: (res: HttpResponse<IPersonalData[]>) => {
        this.isLoading = false;
        this.personalData = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPersonalData): number {
    return item.id!;
  }

  delete(personalData: IPersonalData): void {
    const modalRef = this.modalService.open(PersonalDataDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.personalData = personalData;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
