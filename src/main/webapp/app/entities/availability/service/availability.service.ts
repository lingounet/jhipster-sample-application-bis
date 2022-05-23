import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAvailability, getAvailabilityIdentifier } from '../availability.model';

export type EntityResponseType = HttpResponse<IAvailability>;
export type EntityArrayResponseType = HttpResponse<IAvailability[]>;

@Injectable({ providedIn: 'root' })
export class AvailabilityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/availabilities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(availability: IAvailability): Observable<EntityResponseType> {
    return this.http.post<IAvailability>(this.resourceUrl, availability, { observe: 'response' });
  }

  update(availability: IAvailability): Observable<EntityResponseType> {
    return this.http.put<IAvailability>(`${this.resourceUrl}/${getAvailabilityIdentifier(availability) as number}`, availability, {
      observe: 'response',
    });
  }

  partialUpdate(availability: IAvailability): Observable<EntityResponseType> {
    return this.http.patch<IAvailability>(`${this.resourceUrl}/${getAvailabilityIdentifier(availability) as number}`, availability, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAvailability>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAvailability[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAvailabilityToCollectionIfMissing(
    availabilityCollection: IAvailability[],
    ...availabilitiesToCheck: (IAvailability | null | undefined)[]
  ): IAvailability[] {
    const availabilities: IAvailability[] = availabilitiesToCheck.filter(isPresent);
    if (availabilities.length > 0) {
      const availabilityCollectionIdentifiers = availabilityCollection.map(
        availabilityItem => getAvailabilityIdentifier(availabilityItem)!
      );
      const availabilitiesToAdd = availabilities.filter(availabilityItem => {
        const availabilityIdentifier = getAvailabilityIdentifier(availabilityItem);
        if (availabilityIdentifier == null || availabilityCollectionIdentifiers.includes(availabilityIdentifier)) {
          return false;
        }
        availabilityCollectionIdentifiers.push(availabilityIdentifier);
        return true;
      });
      return [...availabilitiesToAdd, ...availabilityCollection];
    }
    return availabilityCollection;
  }
}
