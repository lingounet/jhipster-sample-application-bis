import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHostingPlatform, HostingPlatform } from '../hosting-platform.model';
import { HostingPlatformService } from '../service/hosting-platform.service';

@Injectable({ providedIn: 'root' })
export class HostingPlatformRoutingResolveService implements Resolve<IHostingPlatform> {
  constructor(protected service: HostingPlatformService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHostingPlatform> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((hostingPlatform: HttpResponse<HostingPlatform>) => {
          if (hostingPlatform.body) {
            return of(hostingPlatform.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new HostingPlatform());
  }
}
