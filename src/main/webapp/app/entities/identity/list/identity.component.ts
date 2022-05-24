import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IIdentity } from '../identity.model';
import { IdentityService } from '../service/identity.service';
import { IdentityDeleteDialogComponent } from '../delete/identity-delete-dialog.component';

@Component({
  selector: 'jhi-identity',
  templateUrl: './identity.component.html',
})
export class IdentityComponent implements OnInit {
  identities?: IIdentity[];
  isLoading = false;

  constructor(protected identityService: IdentityService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.identityService.query().subscribe({
      next: (res: HttpResponse<IIdentity[]>) => {
        this.isLoading = false;
        this.identities = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IIdentity): number {
    return item.id!;
  }

  delete(identity: IIdentity): void {
    const modalRef = this.modalService.open(IdentityDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.identity = identity;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
