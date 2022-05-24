import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAvailability, Availability } from '../availability.model';
import { AvailabilityService } from '../service/availability.service';

@Injectable({ providedIn: 'root' })
export class AvailabilityRoutingResolveService implements Resolve<IAvailability> {
  constructor(protected service: AvailabilityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAvailability> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((availability: HttpResponse<Availability>) => {
          if (availability.body) {
            return of(availability.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Availability());
  }
}
