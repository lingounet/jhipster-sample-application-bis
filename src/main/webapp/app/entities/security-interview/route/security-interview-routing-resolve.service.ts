import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISecurityInterview, SecurityInterview } from '../security-interview.model';
import { SecurityInterviewService } from '../service/security-interview.service';

@Injectable({ providedIn: 'root' })
export class SecurityInterviewRoutingResolveService implements Resolve<ISecurityInterview> {
  constructor(protected service: SecurityInterviewService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISecurityInterview> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((securityInterview: HttpResponse<SecurityInterview>) => {
          if (securityInterview.body) {
            return of(securityInterview.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SecurityInterview());
  }
}
