import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonalDataType } from '../personal-data-type.model';

@Component({
  selector: 'jhi-personal-data-type-detail',
  templateUrl: './personal-data-type-detail.component.html',
})
export class PersonalDataTypeDetailComponent implements OnInit {
  personalDataType: IPersonalDataType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personalDataType }) => {
      this.personalDataType = personalDataType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
