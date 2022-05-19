import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISecurityInterview } from '../security-interview.model';
import { SecurityInterviewService } from '../service/security-interview.service';
import { SecurityInterviewDeleteDialogComponent } from '../delete/security-interview-delete-dialog.component';

@Component({
  selector: 'jhi-security-interview',
  templateUrl: './security-interview.component.html',
})
export class SecurityInterviewComponent implements OnInit {
  securityInterviews?: ISecurityInterview[];
  isLoading = false;

  constructor(protected securityInterviewService: SecurityInterviewService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.securityInterviewService.query().subscribe({
      next: (res: HttpResponse<ISecurityInterview[]>) => {
        this.isLoading = false;
        this.securityInterviews = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ISecurityInterview): number {
    return item.id!;
  }

  delete(securityInterview: ISecurityInterview): void {
    const modalRef = this.modalService.open(SecurityInterviewDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.securityInterview = securityInterview;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
