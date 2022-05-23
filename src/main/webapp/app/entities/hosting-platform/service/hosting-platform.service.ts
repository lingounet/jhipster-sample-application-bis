import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHostingPlatform, getHostingPlatformIdentifier } from '../hosting-platform.model';

export type EntityResponseType = HttpResponse<IHostingPlatform>;
export type EntityArrayResponseType = HttpResponse<IHostingPlatform[]>;

@Injectable({ providedIn: 'root' })
export class HostingPlatformService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/hosting-platforms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(hostingPlatform: IHostingPlatform): Observable<EntityResponseType> {
    return this.http.post<IHostingPlatform>(this.resourceUrl, hostingPlatform, { observe: 'response' });
  }

  update(hostingPlatform: IHostingPlatform): Observable<EntityResponseType> {
    return this.http.put<IHostingPlatform>(
      `${this.resourceUrl}/${getHostingPlatformIdentifier(hostingPlatform) as number}`,
      hostingPlatform,
      { observe: 'response' }
    );
  }

  partialUpdate(hostingPlatform: IHostingPlatform): Observable<EntityResponseType> {
    return this.http.patch<IHostingPlatform>(
      `${this.resourceUrl}/${getHostingPlatformIdentifier(hostingPlatform) as number}`,
      hostingPlatform,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHostingPlatform>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHostingPlatform[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addHostingPlatformToCollectionIfMissing(
    hostingPlatformCollection: IHostingPlatform[],
    ...hostingPlatformsToCheck: (IHostingPlatform | null | undefined)[]
  ): IHostingPlatform[] {
    const hostingPlatforms: IHostingPlatform[] = hostingPlatformsToCheck.filter(isPresent);
    if (hostingPlatforms.length > 0) {
      const hostingPlatformCollectionIdentifiers = hostingPlatformCollection.map(
        hostingPlatformItem => getHostingPlatformIdentifier(hostingPlatformItem)!
      );
      const hostingPlatformsToAdd = hostingPlatforms.filter(hostingPlatformItem => {
        const hostingPlatformIdentifier = getHostingPlatformIdentifier(hostingPlatformItem);
        if (hostingPlatformIdentifier == null || hostingPlatformCollectionIdentifiers.includes(hostingPlatformIdentifier)) {
          return false;
        }
        hostingPlatformCollectionIdentifiers.push(hostingPlatformIdentifier);
        return true;
      });
      return [...hostingPlatformsToAdd, ...hostingPlatformCollection];
    }
    return hostingPlatformCollection;
  }
}
