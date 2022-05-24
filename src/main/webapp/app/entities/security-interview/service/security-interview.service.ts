import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISecurityInterview, getSecurityInterviewIdentifier } from '../security-interview.model';

export type EntityResponseType = HttpResponse<ISecurityInterview>;
export type EntityArrayResponseType = HttpResponse<ISecurityInterview[]>;

@Injectable({ providedIn: 'root' })
export class SecurityInterviewService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/security-interviews');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(securityInterview: ISecurityInterview): Observable<EntityResponseType> {
    return this.http.post<ISecurityInterview>(this.resourceUrl, securityInterview, { observe: 'response' });
  }

  update(securityInterview: ISecurityInterview): Observable<EntityResponseType> {
    return this.http.put<ISecurityInterview>(
      `${this.resourceUrl}/${getSecurityInterviewIdentifier(securityInterview) as number}`,
      securityInterview,
      { observe: 'response' }
    );
  }

  partialUpdate(securityInterview: ISecurityInterview): Observable<EntityResponseType> {
    return this.http.patch<ISecurityInterview>(
      `${this.resourceUrl}/${getSecurityInterviewIdentifier(securityInterview) as number}`,
      securityInterview,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISecurityInterview>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISecurityInterview[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSecurityInterviewToCollectionIfMissing(
    securityInterviewCollection: ISecurityInterview[],
    ...securityInterviewsToCheck: (ISecurityInterview | null | undefined)[]
  ): ISecurityInterview[] {
    const securityInterviews: ISecurityInterview[] = securityInterviewsToCheck.filter(isPresent);
    if (securityInterviews.length > 0) {
      const securityInterviewCollectionIdentifiers = securityInterviewCollection.map(
        securityInterviewItem => getSecurityInterviewIdentifier(securityInterviewItem)!
      );
      const securityInterviewsToAdd = securityInterviews.filter(securityInterviewItem => {
        const securityInterviewIdentifier = getSecurityInterviewIdentifier(securityInterviewItem);
        if (securityInterviewIdentifier == null || securityInterviewCollectionIdentifiers.includes(securityInterviewIdentifier)) {
          return false;
        }
        securityInterviewCollectionIdentifiers.push(securityInterviewIdentifier);
        return true;
      });
      return [...securityInterviewsToAdd, ...securityInterviewCollection];
    }
    return securityInterviewCollection;
  }
}
