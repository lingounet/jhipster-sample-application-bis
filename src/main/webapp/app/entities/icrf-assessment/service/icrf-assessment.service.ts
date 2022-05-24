import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIcrfAssessment, getIcrfAssessmentIdentifier } from '../icrf-assessment.model';

export type EntityResponseType = HttpResponse<IIcrfAssessment>;
export type EntityArrayResponseType = HttpResponse<IIcrfAssessment[]>;

@Injectable({ providedIn: 'root' })
export class IcrfAssessmentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/icrf-assessments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(icrfAssessment: IIcrfAssessment): Observable<EntityResponseType> {
    return this.http.post<IIcrfAssessment>(this.resourceUrl, icrfAssessment, { observe: 'response' });
  }

  update(icrfAssessment: IIcrfAssessment): Observable<EntityResponseType> {
    return this.http.put<IIcrfAssessment>(`${this.resourceUrl}/${getIcrfAssessmentIdentifier(icrfAssessment) as number}`, icrfAssessment, {
      observe: 'response',
    });
  }

  partialUpdate(icrfAssessment: IIcrfAssessment): Observable<EntityResponseType> {
    return this.http.patch<IIcrfAssessment>(
      `${this.resourceUrl}/${getIcrfAssessmentIdentifier(icrfAssessment) as number}`,
      icrfAssessment,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIcrfAssessment>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIcrfAssessment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIcrfAssessmentToCollectionIfMissing(
    icrfAssessmentCollection: IIcrfAssessment[],
    ...icrfAssessmentsToCheck: (IIcrfAssessment | null | undefined)[]
  ): IIcrfAssessment[] {
    const icrfAssessments: IIcrfAssessment[] = icrfAssessmentsToCheck.filter(isPresent);
    if (icrfAssessments.length > 0) {
      const icrfAssessmentCollectionIdentifiers = icrfAssessmentCollection.map(
        icrfAssessmentItem => getIcrfAssessmentIdentifier(icrfAssessmentItem)!
      );
      const icrfAssessmentsToAdd = icrfAssessments.filter(icrfAssessmentItem => {
        const icrfAssessmentIdentifier = getIcrfAssessmentIdentifier(icrfAssessmentItem);
        if (icrfAssessmentIdentifier == null || icrfAssessmentCollectionIdentifiers.includes(icrfAssessmentIdentifier)) {
          return false;
        }
        icrfAssessmentCollectionIdentifiers.push(icrfAssessmentIdentifier);
        return true;
      });
      return [...icrfAssessmentsToAdd, ...icrfAssessmentCollection];
    }
    return icrfAssessmentCollection;
  }
}
