import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Criticality } from 'app/entities/enumerations/criticality.model';
import { IAvailability, Availability } from '../availability.model';

import { AvailabilityService } from './availability.service';

describe('Availability Service', () => {
  let service: AvailabilityService;
  let httpMock: HttpTestingController;
  let elemDefault: IAvailability;
  let expectedResult: IAvailability | IAvailability[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AvailabilityService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      financial: Criticality.LOW,
      legal: Criticality.LOW,
      image: Criticality.LOW,
      strategy: Criticality.LOW,
      operational: Criticality.LOW,
      traceabilityContentType: 'image/png',
      traceability: 'AAAAAAA',
      confidentialityContentType: 'image/png',
      confidentiality: 'AAAAAAA',
      integrityContentType: 'image/png',
      integrity: 'AAAAAAA',
      critical: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Availability', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Availability()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Availability', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          financial: 'BBBBBB',
          legal: 'BBBBBB',
          image: 'BBBBBB',
          strategy: 'BBBBBB',
          operational: 'BBBBBB',
          traceability: 'BBBBBB',
          confidentiality: 'BBBBBB',
          integrity: 'BBBBBB',
          critical: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Availability', () => {
      const patchObject = Object.assign(
        {
          financial: 'BBBBBB',
          legal: 'BBBBBB',
          image: 'BBBBBB',
          confidentiality: 'BBBBBB',
          integrity: 'BBBBBB',
          critical: true,
        },
        new Availability()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Availability', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          financial: 'BBBBBB',
          legal: 'BBBBBB',
          image: 'BBBBBB',
          strategy: 'BBBBBB',
          operational: 'BBBBBB',
          traceability: 'BBBBBB',
          confidentiality: 'BBBBBB',
          integrity: 'BBBBBB',
          critical: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Availability', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAvailabilityToCollectionIfMissing', () => {
      it('should add a Availability to an empty array', () => {
        const availability: IAvailability = { id: 123 };
        expectedResult = service.addAvailabilityToCollectionIfMissing([], availability);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(availability);
      });

      it('should not add a Availability to an array that contains it', () => {
        const availability: IAvailability = { id: 123 };
        const availabilityCollection: IAvailability[] = [
          {
            ...availability,
          },
          { id: 456 },
        ];
        expectedResult = service.addAvailabilityToCollectionIfMissing(availabilityCollection, availability);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Availability to an array that doesn't contain it", () => {
        const availability: IAvailability = { id: 123 };
        const availabilityCollection: IAvailability[] = [{ id: 456 }];
        expectedResult = service.addAvailabilityToCollectionIfMissing(availabilityCollection, availability);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(availability);
      });

      it('should add only unique Availability to an array', () => {
        const availabilityArray: IAvailability[] = [{ id: 123 }, { id: 456 }, { id: 84689 }];
        const availabilityCollection: IAvailability[] = [{ id: 123 }];
        expectedResult = service.addAvailabilityToCollectionIfMissing(availabilityCollection, ...availabilityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const availability: IAvailability = { id: 123 };
        const availability2: IAvailability = { id: 456 };
        expectedResult = service.addAvailabilityToCollectionIfMissing([], availability, availability2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(availability);
        expect(expectedResult).toContain(availability2);
      });

      it('should accept null and undefined values', () => {
        const availability: IAvailability = { id: 123 };
        expectedResult = service.addAvailabilityToCollectionIfMissing([], null, availability, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(availability);
      });

      it('should return initial array if no Availability is added', () => {
        const availabilityCollection: IAvailability[] = [{ id: 123 }];
        expectedResult = service.addAvailabilityToCollectionIfMissing(availabilityCollection, undefined, null);
        expect(expectedResult).toEqual(availabilityCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
