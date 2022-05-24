import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IApplicationType } from '../application-type.model';

@Component({
  selector: 'jhi-application-type-detail',
  templateUrl: './application-type-detail.component.html',
})
export class ApplicationTypeDetailComponent implements OnInit {
  applicationType: IApplicationType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicationType }) => {
      this.applicationType = applicationType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
