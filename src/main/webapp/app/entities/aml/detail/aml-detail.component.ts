import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAml } from '../aml.model';

@Component({
  selector: 'jhi-aml-detail',
  templateUrl: './aml-detail.component.html',
})
export class AmlDetailComponent implements OnInit {
  aml: IAml | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aml }) => {
      this.aml = aml;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
