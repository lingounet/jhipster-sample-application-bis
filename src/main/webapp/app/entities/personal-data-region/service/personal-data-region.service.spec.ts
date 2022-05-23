import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPersonalDataRegion, PersonalDataRegion } from '../personal-data-region.model';

import { PersonalDataRegionService } from './personal-data-region.service';

describe('PersonalDataRegion Service', () => {
  let service: PersonalDataRegionService;
  let httpMock: HttpTestingController;
  let elemDefault: IPersonalDataRegion;
  let expectedResult: IPersonalDataRegion | IPersonalDataRegion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PersonalDataRegionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
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

    it('should create a PersonalDataRegion', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PersonalDataRegion()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PersonalDataRegion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PersonalDataRegion', () => {
      const patchObject = Object.assign({}, new PersonalDataRegion());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PersonalDataRegion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
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

    it('should delete a PersonalDataRegion', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPersonalDataRegionToCollectionIfMissing', () => {
      it('should add a PersonalDataRegion to an empty array', () => {
        const personalDataRegion: IPersonalDataRegion = { id: 123 };
        expectedResult = service.addPersonalDataRegionToCollectionIfMissing([], personalDataRegion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personalDataRegion);
      });

      it('should not add a PersonalDataRegion to an array that contains it', () => {
        const personalDataRegion: IPersonalDataRegion = { id: 123 };
        const personalDataRegionCollection: IPersonalDataRegion[] = [
          {
            ...personalDataRegion,
          },
          { id: 456 },
        ];
        expectedResult = service.addPersonalDataRegionToCollectionIfMissing(personalDataRegionCollection, personalDataRegion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PersonalDataRegion to an array that doesn't contain it", () => {
        const personalDataRegion: IPersonalDataRegion = { id: 123 };
        const personalDataRegionCollection: IPersonalDataRegion[] = [{ id: 456 }];
        expectedResult = service.addPersonalDataRegionToCollectionIfMissing(personalDataRegionCollection, personalDataRegion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personalDataRegion);
      });

      it('should add only unique PersonalDataRegion to an array', () => {
        const personalDataRegionArray: IPersonalDataRegion[] = [{ id: 123 }, { id: 456 }, { id: 73186 }];
        const personalDataRegionCollection: IPersonalDataRegion[] = [{ id: 123 }];
        expectedResult = service.addPersonalDataRegionToCollectionIfMissing(personalDataRegionCollection, ...personalDataRegionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const personalDataRegion: IPersonalDataRegion = { id: 123 };
        const personalDataRegion2: IPersonalDataRegion = { id: 456 };
        expectedResult = service.addPersonalDataRegionToCollectionIfMissing([], personalDataRegion, personalDataRegion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personalDataRegion);
        expect(expectedResult).toContain(personalDataRegion2);
      });

      it('should accept null and undefined values', () => {
        const personalDataRegion: IPersonalDataRegion = { id: 123 };
        expectedResult = service.addPersonalDataRegionToCollectionIfMissing([], null, personalDataRegion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personalDataRegion);
      });

      it('should return initial array if no PersonalDataRegion is added', () => {
        const personalDataRegionCollection: IPersonalDataRegion[] = [{ id: 123 }];
        expectedResult = service.addPersonalDataRegionToCollectionIfMissing(personalDataRegionCollection, undefined, null);
        expect(expectedResult).toEqual(personalDataRegionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
