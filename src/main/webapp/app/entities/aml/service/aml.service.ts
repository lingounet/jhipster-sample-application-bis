import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAml, getAmlIdentifier } from '../aml.model';

export type EntityResponseType = HttpResponse<IAml>;
export type EntityArrayResponseType = HttpResponse<IAml[]>;

@Injectable({ providedIn: 'root' })
export class AmlService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/amls');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aml: IAml): Observable<EntityResponseType> {
    return this.http.post<IAml>(this.resourceUrl, aml, { observe: 'response' });
  }

  update(aml: IAml): Observable<EntityResponseType> {
    return this.http.put<IAml>(`${this.resourceUrl}/${getAmlIdentifier(aml) as number}`, aml, { observe: 'response' });
  }

  partialUpdate(aml: IAml): Observable<EntityResponseType> {
    return this.http.patch<IAml>(`${this.resourceUrl}/${getAmlIdentifier(aml) as number}`, aml, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAml>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAml[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAmlToCollectionIfMissing(amlCollection: IAml[], ...amlsToCheck: (IAml | null | undefined)[]): IAml[] {
    const amls: IAml[] = amlsToCheck.filter(isPresent);
    if (amls.length > 0) {
      const amlCollectionIdentifiers = amlCollection.map(amlItem => getAmlIdentifier(amlItem)!);
      const amlsToAdd = amls.filter(amlItem => {
        const amlIdentifier = getAmlIdentifier(amlItem);
        if (amlIdentifier == null || amlCollectionIdentifiers.includes(amlIdentifier)) {
          return false;
        }
        amlCollectionIdentifiers.push(amlIdentifier);
        return true;
      });
      return [...amlsToAdd, ...amlCollection];
    }
    return amlCollection;
  }
}
