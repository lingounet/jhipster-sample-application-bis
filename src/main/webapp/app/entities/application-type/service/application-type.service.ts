import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApplicationType, getApplicationTypeIdentifier } from '../application-type.model';

export type EntityResponseType = HttpResponse<IApplicationType>;
export type EntityArrayResponseType = HttpResponse<IApplicationType[]>;

@Injectable({ providedIn: 'root' })
export class ApplicationTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/application-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(applicationType: IApplicationType): Observable<EntityResponseType> {
    return this.http.post<IApplicationType>(this.resourceUrl, applicationType, { observe: 'response' });
  }

  update(applicationType: IApplicationType): Observable<EntityResponseType> {
    return this.http.put<IApplicationType>(
      `${this.resourceUrl}/${getApplicationTypeIdentifier(applicationType) as number}`,
      applicationType,
      { observe: 'response' }
    );
  }

  partialUpdate(applicationType: IApplicationType): Observable<EntityResponseType> {
    return this.http.patch<IApplicationType>(
      `${this.resourceUrl}/${getApplicationTypeIdentifier(applicationType) as number}`,
      applicationType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IApplicationType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApplicationType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addApplicationTypeToCollectionIfMissing(
    applicationTypeCollection: IApplicationType[],
    ...applicationTypesToCheck: (IApplicationType | null | undefined)[]
  ): IApplicationType[] {
    const applicationTypes: IApplicationType[] = applicationTypesToCheck.filter(isPresent);
    if (applicationTypes.length > 0) {
      const applicationTypeCollectionIdentifiers = applicationTypeCollection.map(
        applicationTypeItem => getApplicationTypeIdentifier(applicationTypeItem)!
      );
      const applicationTypesToAdd = applicationTypes.filter(applicationTypeItem => {
        const applicationTypeIdentifier = getApplicationTypeIdentifier(applicationTypeItem);
        if (applicationTypeIdentifier == null || applicationTypeCollectionIdentifiers.includes(applicationTypeIdentifier)) {
          return false;
        }
        applicationTypeCollectionIdentifiers.push(applicationTypeIdentifier);
        return true;
      });
      return [...applicationTypesToAdd, ...applicationTypeCollection];
    }
    return applicationTypeCollection;
  }
}
