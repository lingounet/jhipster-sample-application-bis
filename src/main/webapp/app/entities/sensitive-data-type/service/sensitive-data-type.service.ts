import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISensitiveDataType, getSensitiveDataTypeIdentifier } from '../sensitive-data-type.model';

export type EntityResponseType = HttpResponse<ISensitiveDataType>;
export type EntityArrayResponseType = HttpResponse<ISensitiveDataType[]>;

@Injectable({ providedIn: 'root' })
export class SensitiveDataTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sensitive-data-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sensitiveDataType: ISensitiveDataType): Observable<EntityResponseType> {
    return this.http.post<ISensitiveDataType>(this.resourceUrl, sensitiveDataType, { observe: 'response' });
  }

  update(sensitiveDataType: ISensitiveDataType): Observable<EntityResponseType> {
    return this.http.put<ISensitiveDataType>(
      `${this.resourceUrl}/${getSensitiveDataTypeIdentifier(sensitiveDataType) as number}`,
      sensitiveDataType,
      { observe: 'response' }
    );
  }

  partialUpdate(sensitiveDataType: ISensitiveDataType): Observable<EntityResponseType> {
    return this.http.patch<ISensitiveDataType>(
      `${this.resourceUrl}/${getSensitiveDataTypeIdentifier(sensitiveDataType) as number}`,
      sensitiveDataType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISensitiveDataType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISensitiveDataType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSensitiveDataTypeToCollectionIfMissing(
    sensitiveDataTypeCollection: ISensitiveDataType[],
    ...sensitiveDataTypesToCheck: (ISensitiveDataType | null | undefined)[]
  ): ISensitiveDataType[] {
    const sensitiveDataTypes: ISensitiveDataType[] = sensitiveDataTypesToCheck.filter(isPresent);
    if (sensitiveDataTypes.length > 0) {
      const sensitiveDataTypeCollectionIdentifiers = sensitiveDataTypeCollection.map(
        sensitiveDataTypeItem => getSensitiveDataTypeIdentifier(sensitiveDataTypeItem)!
      );
      const sensitiveDataTypesToAdd = sensitiveDataTypes.filter(sensitiveDataTypeItem => {
        const sensitiveDataTypeIdentifier = getSensitiveDataTypeIdentifier(sensitiveDataTypeItem);
        if (sensitiveDataTypeIdentifier == null || sensitiveDataTypeCollectionIdentifiers.includes(sensitiveDataTypeIdentifier)) {
          return false;
        }
        sensitiveDataTypeCollectionIdentifiers.push(sensitiveDataTypeIdentifier);
        return true;
      });
      return [...sensitiveDataTypesToAdd, ...sensitiveDataTypeCollection];
    }
    return sensitiveDataTypeCollection;
  }
}
