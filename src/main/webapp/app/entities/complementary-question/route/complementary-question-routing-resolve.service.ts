import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IComplementaryQuestion, ComplementaryQuestion } from '../complementary-question.model';
import { ComplementaryQuestionService } from '../service/complementary-question.service';

@Injectable({ providedIn: 'root' })
export class ComplementaryQuestionRoutingResolveService implements Resolve<IComplementaryQuestion> {
  constructor(protected service: ComplementaryQuestionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IComplementaryQuestion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((complementaryQuestion: HttpResponse<ComplementaryQuestion>) => {
          if (complementaryQuestion.body) {
            return of(complementaryQuestion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ComplementaryQuestion());
  }
}
