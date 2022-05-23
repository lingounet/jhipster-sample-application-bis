import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIcrfStatus } from '../icrf-status.model';

@Component({
  selector: 'jhi-icrf-status-detail',
  templateUrl: './icrf-status-detail.component.html',
})
export class IcrfStatusDetailComponent implements OnInit {
  icrfStatus: IIcrfStatus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ icrfStatus }) => {
      this.icrfStatus = icrfStatus;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
