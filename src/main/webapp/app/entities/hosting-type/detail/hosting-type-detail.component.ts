import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHostingType } from '../hosting-type.model';

@Component({
  selector: 'jhi-hosting-type-detail',
  templateUrl: './hosting-type-detail.component.html',
})
export class HostingTypeDetailComponent implements OnInit {
  hostingType: IHostingType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hostingType }) => {
      this.hostingType = hostingType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
