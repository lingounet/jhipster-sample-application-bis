import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPsat, getPsatIdentifier } from '../psat.model';

export type EntityResponseType = HttpResponse<IPsat>;
export type EntityArrayResponseType = HttpResponse<IPsat[]>;

@Injectable({ providedIn: 'root' })
export class PsatService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/psats');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(psat: IPsat): Observable<EntityResponseType> {
    return this.http.post<IPsat>(this.resourceUrl, psat, { observe: 'response' });
  }

  update(psat: IPsat): Observable<EntityResponseType> {
    return this.http.put<IPsat>(`${this.resourceUrl}/${getPsatIdentifier(psat) as number}`, psat, { observe: 'response' });
  }

  partialUpdate(psat: IPsat): Observable<EntityResponseType> {
    return this.http.patch<IPsat>(`${this.resourceUrl}/${getPsatIdentifier(psat) as number}`, psat, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPsat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPsat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPsatToCollectionIfMissing(psatCollection: IPsat[], ...psatsToCheck: (IPsat | null | undefined)[]): IPsat[] {
    const psats: IPsat[] = psatsToCheck.filter(isPresent);
    if (psats.length > 0) {
      const psatCollectionIdentifiers = psatCollection.map(psatItem => getPsatIdentifier(psatItem)!);
      const psatsToAdd = psats.filter(psatItem => {
        const psatIdentifier = getPsatIdentifier(psatItem);
        if (psatIdentifier == null || psatCollectionIdentifiers.includes(psatIdentifier)) {
          return false;
        }
        psatCollectionIdentifiers.push(psatIdentifier);
        return true;
      });
      return [...psatsToAdd, ...psatCollection];
    }
    return psatCollection;
  }
}
