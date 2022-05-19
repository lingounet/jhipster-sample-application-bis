import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHosting, getHostingIdentifier } from '../hosting.model';

export type EntityResponseType = HttpResponse<IHosting>;
export type EntityArrayResponseType = HttpResponse<IHosting[]>;

@Injectable({ providedIn: 'root' })
export class HostingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/hostings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(hosting: IHosting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hosting);
    return this.http
      .post<IHosting>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(hosting: IHosting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hosting);
    return this.http
      .put<IHosting>(`${this.resourceUrl}/${getHostingIdentifier(hosting) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(hosting: IHosting): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hosting);
    return this.http
      .patch<IHosting>(`${this.resourceUrl}/${getHostingIdentifier(hosting) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IHosting>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHosting[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addHostingToCollectionIfMissing(hostingCollection: IHosting[], ...hostingsToCheck: (IHosting | null | undefined)[]): IHosting[] {
    const hostings: IHosting[] = hostingsToCheck.filter(isPresent);
    if (hostings.length > 0) {
      const hostingCollectionIdentifiers = hostingCollection.map(hostingItem => getHostingIdentifier(hostingItem)!);
      const hostingsToAdd = hostings.filter(hostingItem => {
        const hostingIdentifier = getHostingIdentifier(hostingItem);
        if (hostingIdentifier == null || hostingCollectionIdentifiers.includes(hostingIdentifier)) {
          return false;
        }
        hostingCollectionIdentifiers.push(hostingIdentifier);
        return true;
      });
      return [...hostingsToAdd, ...hostingCollection];
    }
    return hostingCollection;
  }

  protected convertDateFromClient(hosting: IHosting): IHosting {
    return Object.assign({}, hosting, {
      date: hosting.date?.isValid() ? hosting.date.toJSON() : undefined,
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
      res.body.forEach((hosting: IHosting) => {
        hosting.date = hosting.date ? dayjs(hosting.date) : undefined;
      });
    }
    return res;
  }
}
