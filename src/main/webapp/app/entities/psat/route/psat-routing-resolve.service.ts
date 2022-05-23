import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPsat, Psat } from '../psat.model';
import { PsatService } from '../service/psat.service';

@Injectable({ providedIn: 'root' })
export class PsatRoutingResolveService implements Resolve<IPsat> {
  constructor(protected service: PsatService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPsat> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((psat: HttpResponse<Psat>) => {
          if (psat.body) {
            return of(psat.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Psat());
  }
}
