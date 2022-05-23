import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonalDataRegion } from '../personal-data-region.model';

@Component({
  selector: 'jhi-personal-data-region-detail',
  templateUrl: './personal-data-region-detail.component.html',
})
export class PersonalDataRegionDetailComponent implements OnInit {
  personalDataRegion: IPersonalDataRegion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personalDataRegion }) => {
      this.personalDataRegion = personalDataRegion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
