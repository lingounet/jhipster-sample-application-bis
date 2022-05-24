import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIcrfStatus, getIcrfStatusIdentifier } from '../icrf-status.model';

export type EntityResponseType = HttpResponse<IIcrfStatus>;
export type EntityArrayResponseType = HttpResponse<IIcrfStatus[]>;

@Injectable({ providedIn: 'root' })
export class IcrfStatusService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/icrf-statuses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(icrfStatus: IIcrfStatus): Observable<EntityResponseType> {
    return this.http.post<IIcrfStatus>(this.resourceUrl, icrfStatus, { observe: 'response' });
  }

  update(icrfStatus: IIcrfStatus): Observable<EntityResponseType> {
    return this.http.put<IIcrfStatus>(`${this.resourceUrl}/${getIcrfStatusIdentifier(icrfStatus) as number}`, icrfStatus, {
      observe: 'response',
    });
  }

  partialUpdate(icrfStatus: IIcrfStatus): Observable<EntityResponseType> {
    return this.http.patch<IIcrfStatus>(`${this.resourceUrl}/${getIcrfStatusIdentifier(icrfStatus) as number}`, icrfStatus, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIcrfStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIcrfStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIcrfStatusToCollectionIfMissing(
    icrfStatusCollection: IIcrfStatus[],
    ...icrfStatusesToCheck: (IIcrfStatus | null | undefined)[]
  ): IIcrfStatus[] {
    const icrfStatuses: IIcrfStatus[] = icrfStatusesToCheck.filter(isPresent);
    if (icrfStatuses.length > 0) {
      const icrfStatusCollectionIdentifiers = icrfStatusCollection.map(icrfStatusItem => getIcrfStatusIdentifier(icrfStatusItem)!);
      const icrfStatusesToAdd = icrfStatuses.filter(icrfStatusItem => {
        const icrfStatusIdentifier = getIcrfStatusIdentifier(icrfStatusItem);
        if (icrfStatusIdentifier == null || icrfStatusCollectionIdentifiers.includes(icrfStatusIdentifier)) {
          return false;
        }
        icrfStatusCollectionIdentifiers.push(icrfStatusIdentifier);
        return true;
      });
      return [...icrfStatusesToAdd, ...icrfStatusCollection];
    }
    return icrfStatusCollection;
  }
}
