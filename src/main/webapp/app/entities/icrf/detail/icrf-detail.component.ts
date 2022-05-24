import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIcrf } from '../icrf.model';

@Component({
  selector: 'jhi-icrf-detail',
  templateUrl: './icrf-detail.component.html',
})
export class IcrfDetailComponent implements OnInit {
  icrf: IIcrf | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ icrf }) => {
      this.icrf = icrf;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
