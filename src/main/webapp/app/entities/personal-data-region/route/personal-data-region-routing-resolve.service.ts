import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersonalDataRegion, PersonalDataRegion } from '../personal-data-region.model';
import { PersonalDataRegionService } from '../service/personal-data-region.service';

@Injectable({ providedIn: 'root' })
export class PersonalDataRegionRoutingResolveService implements Resolve<IPersonalDataRegion> {
  constructor(protected service: PersonalDataRegionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPersonalDataRegion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((personalDataRegion: HttpResponse<PersonalDataRegion>) => {
          if (personalDataRegion.body) {
            return of(personalDataRegion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PersonalDataRegion());
  }
}
