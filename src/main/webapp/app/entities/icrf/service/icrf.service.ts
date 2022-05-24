import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIcrf, getIcrfIdentifier } from '../icrf.model';

export type EntityResponseType = HttpResponse<IIcrf>;
export type EntityArrayResponseType = HttpResponse<IIcrf[]>;

@Injectable({ providedIn: 'root' })
export class IcrfService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/icrfs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(icrf: IIcrf): Observable<EntityResponseType> {
    return this.http.post<IIcrf>(this.resourceUrl, icrf, { observe: 'response' });
  }

  update(icrf: IIcrf): Observable<EntityResponseType> {
    return this.http.put<IIcrf>(`${this.resourceUrl}/${getIcrfIdentifier(icrf) as number}`, icrf, { observe: 'response' });
  }

  partialUpdate(icrf: IIcrf): Observable<EntityResponseType> {
    return this.http.patch<IIcrf>(`${this.resourceUrl}/${getIcrfIdentifier(icrf) as number}`, icrf, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIcrf>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIcrf[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIcrfToCollectionIfMissing(icrfCollection: IIcrf[], ...icrfsToCheck: (IIcrf | null | undefined)[]): IIcrf[] {
    const icrfs: IIcrf[] = icrfsToCheck.filter(isPresent);
    if (icrfs.length > 0) {
      const icrfCollectionIdentifiers = icrfCollection.map(icrfItem => getIcrfIdentifier(icrfItem)!);
      const icrfsToAdd = icrfs.filter(icrfItem => {
        const icrfIdentifier = getIcrfIdentifier(icrfItem);
        if (icrfIdentifier == null || icrfCollectionIdentifiers.includes(icrfIdentifier)) {
          return false;
        }
        icrfCollectionIdentifiers.push(icrfIdentifier);
        return true;
      });
      return [...icrfsToAdd, ...icrfCollection];
    }
    return icrfCollection;
  }
}
