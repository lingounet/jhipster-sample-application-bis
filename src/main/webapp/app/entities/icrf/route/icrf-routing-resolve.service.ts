import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIcrf, Icrf } from '../icrf.model';
import { IcrfService } from '../service/icrf.service';

@Injectable({ providedIn: 'root' })
export class IcrfRoutingResolveService implements Resolve<IIcrf> {
  constructor(protected service: IcrfService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIcrf> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((icrf: HttpResponse<Icrf>) => {
          if (icrf.body) {
            return of(icrf.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Icrf());
  }
}
