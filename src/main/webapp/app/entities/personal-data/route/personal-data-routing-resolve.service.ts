import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersonalData, PersonalData } from '../personal-data.model';
import { PersonalDataService } from '../service/personal-data.service';

@Injectable({ providedIn: 'root' })
export class PersonalDataRoutingResolveService implements Resolve<IPersonalData> {
  constructor(protected service: PersonalDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPersonalData> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((personalData: HttpResponse<PersonalData>) => {
          if (personalData.body) {
            return of(personalData.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PersonalData());
  }
}
