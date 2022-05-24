import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHostingPlatform } from '../hosting-platform.model';

@Component({
  selector: 'jhi-hosting-platform-detail',
  templateUrl: './hosting-platform-detail.component.html',
})
export class HostingPlatformDetailComponent implements OnInit {
  hostingPlatform: IHostingPlatform | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hostingPlatform }) => {
      this.hostingPlatform = hostingPlatform;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
