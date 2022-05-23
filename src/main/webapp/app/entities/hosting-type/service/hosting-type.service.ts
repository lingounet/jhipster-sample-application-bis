import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHostingType, getHostingTypeIdentifier } from '../hosting-type.model';

export type EntityResponseType = HttpResponse<IHostingType>;
export type EntityArrayResponseType = HttpResponse<IHostingType[]>;

@Injectable({ providedIn: 'root' })
export class HostingTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/hosting-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(hostingType: IHostingType): Observable<EntityResponseType> {
    return this.http.post<IHostingType>(this.resourceUrl, hostingType, { observe: 'response' });
  }

  update(hostingType: IHostingType): Observable<EntityResponseType> {
    return this.http.put<IHostingType>(`${this.resourceUrl}/${getHostingTypeIdentifier(hostingType) as number}`, hostingType, {
      observe: 'response',
    });
  }

  partialUpdate(hostingType: IHostingType): Observable<EntityResponseType> {
    return this.http.patch<IHostingType>(`${this.resourceUrl}/${getHostingTypeIdentifier(hostingType) as number}`, hostingType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHostingType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHostingType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addHostingTypeToCollectionIfMissing(
    hostingTypeCollection: IHostingType[],
    ...hostingTypesToCheck: (IHostingType | null | undefined)[]
  ): IHostingType[] {
    const hostingTypes: IHostingType[] = hostingTypesToCheck.filter(isPresent);
    if (hostingTypes.length > 0) {
      const hostingTypeCollectionIdentifiers = hostingTypeCollection.map(hostingTypeItem => getHostingTypeIdentifier(hostingTypeItem)!);
      const hostingTypesToAdd = hostingTypes.filter(hostingTypeItem => {
        const hostingTypeIdentifier = getHostingTypeIdentifier(hostingTypeItem);
        if (hostingTypeIdentifier == null || hostingTypeCollectionIdentifiers.includes(hostingTypeIdentifier)) {
          return false;
        }
        hostingTypeCollectionIdentifiers.push(hostingTypeIdentifier);
        return true;
      });
      return [...hostingTypesToAdd, ...hostingTypeCollection];
    }
    return hostingTypeCollection;
  }
}
