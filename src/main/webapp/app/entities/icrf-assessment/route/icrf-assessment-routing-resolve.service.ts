import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIcrfAssessment, IcrfAssessment } from '../icrf-assessment.model';
import { IcrfAssessmentService } from '../service/icrf-assessment.service';

@Injectable({ providedIn: 'root' })
export class IcrfAssessmentRoutingResolveService implements Resolve<IIcrfAssessment> {
  constructor(protected service: IcrfAssessmentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIcrfAssessment> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((icrfAssessment: HttpResponse<IcrfAssessment>) => {
          if (icrfAssessment.body) {
            return of(icrfAssessment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new IcrfAssessment());
  }
}
