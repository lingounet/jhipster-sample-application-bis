import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

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
    const copy = this.convertDateFromClient(icrf);
    return this.http
      .post<IIcrf>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(icrf: IIcrf): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(icrf);
    return this.http
      .put<IIcrf>(`${this.resourceUrl}/${getIcrfIdentifier(icrf) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(icrf: IIcrf): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(icrf);
    return this.http
      .patch<IIcrf>(`${this.resourceUrl}/${getIcrfIdentifier(icrf) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IIcrf>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIcrf[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
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

  protected convertDateFromClient(icrf: IIcrf): IIcrf {
    return Object.assign({}, icrf, {
      date: icrf.date?.isValid() ? icrf.date.toJSON() : undefined,
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
      res.body.forEach((icrf: IIcrf) => {
        icrf.date = icrf.date ? dayjs(icrf.date) : undefined;
      });
    }
    return res;
  }
}
