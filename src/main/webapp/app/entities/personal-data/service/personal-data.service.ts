import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonalData, getPersonalDataIdentifier } from '../personal-data.model';

export type EntityResponseType = HttpResponse<IPersonalData>;
export type EntityArrayResponseType = HttpResponse<IPersonalData[]>;

@Injectable({ providedIn: 'root' })
export class PersonalDataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/personal-data');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(personalData: IPersonalData): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personalData);
    return this.http
      .post<IPersonalData>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(personalData: IPersonalData): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personalData);
    return this.http
      .put<IPersonalData>(`${this.resourceUrl}/${getPersonalDataIdentifier(personalData) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(personalData: IPersonalData): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personalData);
    return this.http
      .patch<IPersonalData>(`${this.resourceUrl}/${getPersonalDataIdentifier(personalData) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPersonalData>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPersonalData[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPersonalDataToCollectionIfMissing(
    personalDataCollection: IPersonalData[],
    ...personalDataToCheck: (IPersonalData | null | undefined)[]
  ): IPersonalData[] {
    const personalData: IPersonalData[] = personalDataToCheck.filter(isPresent);
    if (personalData.length > 0) {
      const personalDataCollectionIdentifiers = personalDataCollection.map(
        personalDataItem => getPersonalDataIdentifier(personalDataItem)!
      );
      const personalDataToAdd = personalData.filter(personalDataItem => {
        const personalDataIdentifier = getPersonalDataIdentifier(personalDataItem);
        if (personalDataIdentifier == null || personalDataCollectionIdentifiers.includes(personalDataIdentifier)) {
          return false;
        }
        personalDataCollectionIdentifiers.push(personalDataIdentifier);
        return true;
      });
      return [...personalDataToAdd, ...personalDataCollection];
    }
    return personalDataCollection;
  }

  protected convertDateFromClient(personalData: IPersonalData): IPersonalData {
    return Object.assign({}, personalData, {
      date: personalData.date?.isValid() ? personalData.date.toJSON() : undefined,
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
      res.body.forEach((personalData: IPersonalData) => {
        personalData.date = personalData.date ? dayjs(personalData.date) : undefined;
      });
    }
    return res;
  }
}
