import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPsat } from '../psat.model';

@Component({
  selector: 'jhi-psat-detail',
  templateUrl: './psat-detail.component.html',
})
export class PsatDetailComponent implements OnInit {
  psat: IPsat | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ psat }) => {
      this.psat = psat;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
