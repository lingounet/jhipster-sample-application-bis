import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHosting } from '../hosting.model';

@Component({
  selector: 'jhi-hosting-detail',
  templateUrl: './hosting-detail.component.html',
})
export class HostingDetailComponent implements OnInit {
  hosting: IHosting | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hosting }) => {
      this.hosting = hosting;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
