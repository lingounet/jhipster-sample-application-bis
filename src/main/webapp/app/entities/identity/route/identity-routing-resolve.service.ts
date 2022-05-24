import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIdentity, Identity } from '../identity.model';
import { IdentityService } from '../service/identity.service';

@Injectable({ providedIn: 'root' })
export class IdentityRoutingResolveService implements Resolve<IIdentity> {
  constructor(protected service: IdentityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIdentity> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((identity: HttpResponse<Identity>) => {
          if (identity.body) {
            return of(identity.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Identity());
  }
}
