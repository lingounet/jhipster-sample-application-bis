import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISensitiveDataType } from '../sensitive-data-type.model';

@Component({
  selector: 'jhi-sensitive-data-type-detail',
  templateUrl: './sensitive-data-type-detail.component.html',
})
export class SensitiveDataTypeDetailComponent implements OnInit {
  sensitiveDataType: ISensitiveDataType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sensitiveDataType }) => {
      this.sensitiveDataType = sensitiveDataType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
