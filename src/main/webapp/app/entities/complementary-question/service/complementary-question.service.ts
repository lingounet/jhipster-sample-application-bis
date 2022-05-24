import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IComplementaryQuestion, getComplementaryQuestionIdentifier } from '../complementary-question.model';

export type EntityResponseType = HttpResponse<IComplementaryQuestion>;
export type EntityArrayResponseType = HttpResponse<IComplementaryQuestion[]>;

@Injectable({ providedIn: 'root' })
export class ComplementaryQuestionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/complementary-questions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(complementaryQuestion: IComplementaryQuestion): Observable<EntityResponseType> {
    return this.http.post<IComplementaryQuestion>(this.resourceUrl, complementaryQuestion, { observe: 'response' });
  }

  update(complementaryQuestion: IComplementaryQuestion): Observable<EntityResponseType> {
    return this.http.put<IComplementaryQuestion>(
      `${this.resourceUrl}/${getComplementaryQuestionIdentifier(complementaryQuestion) as number}`,
      complementaryQuestion,
      { observe: 'response' }
    );
  }

  partialUpdate(complementaryQuestion: IComplementaryQuestion): Observable<EntityResponseType> {
    return this.http.patch<IComplementaryQuestion>(
      `${this.resourceUrl}/${getComplementaryQuestionIdentifier(complementaryQuestion) as number}`,
      complementaryQuestion,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IComplementaryQuestion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IComplementaryQuestion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addComplementaryQuestionToCollectionIfMissing(
    complementaryQuestionCollection: IComplementaryQuestion[],
    ...complementaryQuestionsToCheck: (IComplementaryQuestion | null | undefined)[]
  ): IComplementaryQuestion[] {
    const complementaryQuestions: IComplementaryQuestion[] = complementaryQuestionsToCheck.filter(isPresent);
    if (complementaryQuestions.length > 0) {
      const complementaryQuestionCollectionIdentifiers = complementaryQuestionCollection.map(
        complementaryQuestionItem => getComplementaryQuestionIdentifier(complementaryQuestionItem)!
      );
      const complementaryQuestionsToAdd = complementaryQuestions.filter(complementaryQuestionItem => {
        const complementaryQuestionIdentifier = getComplementaryQuestionIdentifier(complementaryQuestionItem);
        if (
          complementaryQuestionIdentifier == null ||
          complementaryQuestionCollectionIdentifiers.includes(complementaryQuestionIdentifier)
        ) {
          return false;
        }
        complementaryQuestionCollectionIdentifiers.push(complementaryQuestionIdentifier);
        return true;
      });
      return [...complementaryQuestionsToAdd, ...complementaryQuestionCollection];
    }
    return complementaryQuestionCollection;
  }
}
