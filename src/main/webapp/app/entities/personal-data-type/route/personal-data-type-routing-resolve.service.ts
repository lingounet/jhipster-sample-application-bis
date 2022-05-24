import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersonalDataType, PersonalDataType } from '../personal-data-type.model';
import { PersonalDataTypeService } from '../service/personal-data-type.service';

@Injectable({ providedIn: 'root' })
export class PersonalDataTypeRoutingResolveService implements Resolve<IPersonalDataType> {
  constructor(protected service: PersonalDataTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPersonalDataType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((personalDataType: HttpResponse<PersonalDataType>) => {
          if (personalDataType.body) {
            return of(personalDataType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PersonalDataType());
  }
}
