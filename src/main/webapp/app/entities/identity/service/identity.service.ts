import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIdentity, getIdentityIdentifier } from '../identity.model';

export type EntityResponseType = HttpResponse<IIdentity>;
export type EntityArrayResponseType = HttpResponse<IIdentity[]>;

@Injectable({ providedIn: 'root' })
export class IdentityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/identities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(identity: IIdentity): Observable<EntityResponseType> {
    return this.http.post<IIdentity>(this.resourceUrl, identity, { observe: 'response' });
  }

  update(identity: IIdentity): Observable<EntityResponseType> {
    return this.http.put<IIdentity>(`${this.resourceUrl}/${getIdentityIdentifier(identity) as number}`, identity, { observe: 'response' });
  }

  partialUpdate(identity: IIdentity): Observable<EntityResponseType> {
    return this.http.patch<IIdentity>(`${this.resourceUrl}/${getIdentityIdentifier(identity) as number}`, identity, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIdentity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIdentity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIdentityToCollectionIfMissing(identityCollection: IIdentity[], ...identitiesToCheck: (IIdentity | null | undefined)[]): IIdentity[] {
    const identities: IIdentity[] = identitiesToCheck.filter(isPresent);
    if (identities.length > 0) {
      const identityCollectionIdentifiers = identityCollection.map(identityItem => getIdentityIdentifier(identityItem)!);
      const identitiesToAdd = identities.filter(identityItem => {
        const identityIdentifier = getIdentityIdentifier(identityItem);
        if (identityIdentifier == null || identityCollectionIdentifiers.includes(identityIdentifier)) {
          return false;
        }
        identityCollectionIdentifiers.push(identityIdentifier);
        return true;
      });
      return [...identitiesToAdd, ...identityCollection];
    }
    return identityCollection;
  }
}
