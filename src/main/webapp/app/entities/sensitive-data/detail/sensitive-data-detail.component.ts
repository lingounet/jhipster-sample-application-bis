import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISensitiveData } from '../sensitive-data.model';

@Component({
  selector: 'jhi-sensitive-data-detail',
  templateUrl: './sensitive-data-detail.component.html',
})
export class SensitiveDataDetailComponent implements OnInit {
  sensitiveData: ISensitiveData | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sensitiveData }) => {
      this.sensitiveData = sensitiveData;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
