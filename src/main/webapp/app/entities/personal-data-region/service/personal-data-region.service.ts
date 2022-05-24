import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonalDataRegion, getPersonalDataRegionIdentifier } from '../personal-data-region.model';

export type EntityResponseType = HttpResponse<IPersonalDataRegion>;
export type EntityArrayResponseType = HttpResponse<IPersonalDataRegion[]>;

@Injectable({ providedIn: 'root' })
export class PersonalDataRegionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/personal-data-regions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(personalDataRegion: IPersonalDataRegion): Observable<EntityResponseType> {
    return this.http.post<IPersonalDataRegion>(this.resourceUrl, personalDataRegion, { observe: 'response' });
  }

  update(personalDataRegion: IPersonalDataRegion): Observable<EntityResponseType> {
    return this.http.put<IPersonalDataRegion>(
      `${this.resourceUrl}/${getPersonalDataRegionIdentifier(personalDataRegion) as number}`,
      personalDataRegion,
      { observe: 'response' }
    );
  }

  partialUpdate(personalDataRegion: IPersonalDataRegion): Observable<EntityResponseType> {
    return this.http.patch<IPersonalDataRegion>(
      `${this.resourceUrl}/${getPersonalDataRegionIdentifier(personalDataRegion) as number}`,
      personalDataRegion,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPersonalDataRegion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersonalDataRegion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPersonalDataRegionToCollectionIfMissing(
    personalDataRegionCollection: IPersonalDataRegion[],
    ...personalDataRegionsToCheck: (IPersonalDataRegion | null | undefined)[]
  ): IPersonalDataRegion[] {
    const personalDataRegions: IPersonalDataRegion[] = personalDataRegionsToCheck.filter(isPresent);
    if (personalDataRegions.length > 0) {
      const personalDataRegionCollectionIdentifiers = personalDataRegionCollection.map(
        personalDataRegionItem => getPersonalDataRegionIdentifier(personalDataRegionItem)!
      );
      const personalDataRegionsToAdd = personalDataRegions.filter(personalDataRegionItem => {
        const personalDataRegionIdentifier = getPersonalDataRegionIdentifier(personalDataRegionItem);
        if (personalDataRegionIdentifier == null || personalDataRegionCollectionIdentifiers.includes(personalDataRegionIdentifier)) {
          return false;
        }
        personalDataRegionCollectionIdentifiers.push(personalDataRegionIdentifier);
        return true;
      });
      return [...personalDataRegionsToAdd, ...personalDataRegionCollection];
    }
    return personalDataRegionCollection;
  }
}
