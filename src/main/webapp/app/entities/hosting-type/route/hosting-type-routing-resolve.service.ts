import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHostingType, HostingType } from '../hosting-type.model';
import { HostingTypeService } from '../service/hosting-type.service';

@Injectable({ providedIn: 'root' })
export class HostingTypeRoutingResolveService implements Resolve<IHostingType> {
  constructor(protected service: HostingTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHostingType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((hostingType: HttpResponse<HostingType>) => {
          if (hostingType.body) {
            return of(hostingType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new HostingType());
  }
}
