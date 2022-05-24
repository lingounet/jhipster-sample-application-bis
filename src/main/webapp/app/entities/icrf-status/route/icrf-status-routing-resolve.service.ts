import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIcrfStatus, IcrfStatus } from '../icrf-status.model';
import { IcrfStatusService } from '../service/icrf-status.service';

@Injectable({ providedIn: 'root' })
export class IcrfStatusRoutingResolveService implements Resolve<IIcrfStatus> {
  constructor(protected service: IcrfStatusService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIcrfStatus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((icrfStatus: HttpResponse<IcrfStatus>) => {
          if (icrfStatus.body) {
            return of(icrfStatus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new IcrfStatus());
  }
}
