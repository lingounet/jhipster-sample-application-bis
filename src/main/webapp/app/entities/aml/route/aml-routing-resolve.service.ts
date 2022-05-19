import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAml, Aml } from '../aml.model';
import { AmlService } from '../service/aml.service';

@Injectable({ providedIn: 'root' })
export class AmlRoutingResolveService implements Resolve<IAml> {
  constructor(protected service: AmlService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAml> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aml: HttpResponse<Aml>) => {
          if (aml.body) {
            return of(aml.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Aml());
  }
}
