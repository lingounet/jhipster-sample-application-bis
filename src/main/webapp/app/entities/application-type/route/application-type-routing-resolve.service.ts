import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApplicationType, ApplicationType } from '../application-type.model';
import { ApplicationTypeService } from '../service/application-type.service';

@Injectable({ providedIn: 'root' })
export class ApplicationTypeRoutingResolveService implements Resolve<IApplicationType> {
  constructor(protected service: ApplicationTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApplicationType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((applicationType: HttpResponse<ApplicationType>) => {
          if (applicationType.body) {
            return of(applicationType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ApplicationType());
  }
}
