import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISensitiveData, getSensitiveDataIdentifier } from '../sensitive-data.model';

export type EntityResponseType = HttpResponse<ISensitiveData>;
export type EntityArrayResponseType = HttpResponse<ISensitiveData[]>;

@Injectable({ providedIn: 'root' })
export class SensitiveDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sensitive-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sensitiveData: ISensitiveData): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sensitiveData);
    return this.http
      .post<ISensitiveData>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(sensitiveData: ISensitiveData): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sensitiveData);
    return this.http
      .put<ISensitiveData>(`${this.resourceUrl}/${getSensitiveDataIdentifier(sensitiveData) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(sensitiveData: ISensitiveData): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sensitiveData);
    return this.http
      .patch<ISensitiveData>(`${this.resourceUrl}/${getSensitiveDataIdentifier(sensitiveData) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISensitiveData>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISensitiveData[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSensitiveDataToCollectionIfMissing(
    sensitiveDataCollection: ISensitiveData[],
    ...sensitiveDataToCheck: (ISensitiveData | null | undefined)[]
  ): ISensitiveData[] {
    const sensitiveData: ISensitiveData[] = sensitiveDataToCheck.filter(isPresent);
    if (sensitiveData.length > 0) {
      const sensitiveDataCollectionIdentifiers = sensitiveDataCollection.map(
        sensitiveDataItem => getSensitiveDataIdentifier(sensitiveDataItem)!
      );
      const sensitiveDataToAdd = sensitiveData.filter(sensitiveDataItem => {
        const sensitiveDataIdentifier = getSensitiveDataIdentifier(sensitiveDataItem);
        if (sensitiveDataIdentifier == null || sensitiveDataCollectionIdentifiers.includes(sensitiveDataIdentifier)) {
          return false;
        }
        sensitiveDataCollectionIdentifiers.push(sensitiveDataIdentifier);
        return true;
      });
      return [...sensitiveDataToAdd, ...sensitiveDataCollection];
    }
    return sensitiveDataCollection;
  }

  protected convertDateFromClient(sensitiveData: ISensitiveData): ISensitiveData {
    return Object.assign({}, sensitiveData, {
      date: sensitiveData.date?.isValid() ? sensitiveData.date.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((sensitiveData: ISensitiveData) => {
        sensitiveData.date = sensitiveData.date ? dayjs(sensitiveData.date) : undefined;
      });
    }
    return res;
  }
}
