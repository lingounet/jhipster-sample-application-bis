import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISecurityInterview } from '../security-interview.model';

@Component({
  selector: 'jhi-security-interview-detail',
  templateUrl: './security-interview-detail.component.html',
})
export class SecurityInterviewDetailComponent implements OnInit {
  securityInterview: ISecurityInterview | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ securityInterview }) => {
      this.securityInterview = securityInterview;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
