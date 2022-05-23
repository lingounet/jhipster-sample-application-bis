import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHosting, Hosting } from '../hosting.model';
import { HostingService } from '../service/hosting.service';

@Injectable({ providedIn: 'root' })
export class HostingRoutingResolveService implements Resolve<IHosting> {
  constructor(protected service: HostingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHosting> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((hosting: HttpResponse<Hosting>) => {
          if (hosting.body) {
            return of(hosting.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Hosting());
  }
}
