import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIdentity } from '../identity.model';

@Component({
  selector: 'jhi-identity-detail',
  templateUrl: './identity-detail.component.html',
})
export class IdentityDetailComponent implements OnInit {
  identity: IIdentity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ identity }) => {
      this.identity = identity;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
