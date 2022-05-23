import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonalDataType, getPersonalDataTypeIdentifier } from '../personal-data-type.model';

export type EntityResponseType = HttpResponse<IPersonalDataType>;
export type EntityArrayResponseType = HttpResponse<IPersonalDataType[]>;

@Injectable({ providedIn: 'root' })
export class PersonalDataTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/personal-data-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(personalDataType: IPersonalDataType): Observable<EntityResponseType> {
    return this.http.post<IPersonalDataType>(this.resourceUrl, personalDataType, { observe: 'response' });
  }

  update(personalDataType: IPersonalDataType): Observable<EntityResponseType> {
    return this.http.put<IPersonalDataType>(
      `${this.resourceUrl}/${getPersonalDataTypeIdentifier(personalDataType) as number}`,
      personalDataType,
      { observe: 'response' }
    );
  }

  partialUpdate(personalDataType: IPersonalDataType): Observable<EntityResponseType> {
    return this.http.patch<IPersonalDataType>(
      `${this.resourceUrl}/${getPersonalDataTypeIdentifier(personalDataType) as number}`,
      personalDataType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPersonalDataType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersonalDataType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPersonalDataTypeToCollectionIfMissing(
    personalDataTypeCollection: IPersonalDataType[],
    ...personalDataTypesToCheck: (IPersonalDataType | null | undefined)[]
  ): IPersonalDataType[] {
    const personalDataTypes: IPersonalDataType[] = personalDataTypesToCheck.filter(isPresent);
    if (personalDataTypes.length > 0) {
      const personalDataTypeCollectionIdentifiers = personalDataTypeCollection.map(
        personalDataTypeItem => getPersonalDataTypeIdentifier(personalDataTypeItem)!
      );
      const personalDataTypesToAdd = personalDataTypes.filter(personalDataTypeItem => {
        const personalDataTypeIdentifier = getPersonalDataTypeIdentifier(personalDataTypeItem);
        if (personalDataTypeIdentifier == null || personalDataTypeCollectionIdentifiers.includes(personalDataTypeIdentifier)) {
          return false;
        }
        personalDataTypeCollectionIdentifiers.push(personalDataTypeIdentifier);
        return true;
      });
      return [...personalDataTypesToAdd, ...personalDataTypeCollection];
    }
    return personalDataTypeCollection;
  }
}
