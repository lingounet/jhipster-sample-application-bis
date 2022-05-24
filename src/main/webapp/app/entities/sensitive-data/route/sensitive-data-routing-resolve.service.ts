import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISensitiveData, SensitiveData } from '../sensitive-data.model';
import { SensitiveDataService } from '../service/sensitive-data.service';

@Injectable({ providedIn: 'root' })
export class SensitiveDataRoutingResolveService implements Resolve<ISensitiveData> {
  constructor(protected service: SensitiveDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISensitiveData> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sensitiveData: HttpResponse<SensitiveData>) => {
          if (sensitiveData.body) {
            return of(sensitiveData.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SensitiveData());
  }
}
