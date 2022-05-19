import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPersonalDataType, PersonalDataType } from '../personal-data-type.model';

import { PersonalDataTypeService } from './personal-data-type.service';

describe('PersonalDataType Service', () => {
  let service: PersonalDataTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IPersonalDataType;
  let expectedResult: IPersonalDataType | IPersonalDataType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PersonalDataTypeService);
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

    it('should create a PersonalDataType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PersonalDataType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PersonalDataType', () => {
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

    it('should partial update a PersonalDataType', () => {
      const patchObject = Object.assign({}, new PersonalDataType());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PersonalDataType', () => {
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

    it('should delete a PersonalDataType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPersonalDataTypeToCollectionIfMissing', () => {
      it('should add a PersonalDataType to an empty array', () => {
        const personalDataType: IPersonalDataType = { id: 123 };
        expectedResult = service.addPersonalDataTypeToCollectionIfMissing([], personalDataType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personalDataType);
      });

      it('should not add a PersonalDataType to an array that contains it', () => {
        const personalDataType: IPersonalDataType = { id: 123 };
        const personalDataTypeCollection: IPersonalDataType[] = [
          {
            ...personalDataType,
          },
          { id: 456 },
        ];
        expectedResult = service.addPersonalDataTypeToCollectionIfMissing(personalDataTypeCollection, personalDataType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PersonalDataType to an array that doesn't contain it", () => {
        const personalDataType: IPersonalDataType = { id: 123 };
        const personalDataTypeCollection: IPersonalDataType[] = [{ id: 456 }];
        expectedResult = service.addPersonalDataTypeToCollectionIfMissing(personalDataTypeCollection, personalDataType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personalDataType);
      });

      it('should add only unique PersonalDataType to an array', () => {
        const personalDataTypeArray: IPersonalDataType[] = [{ id: 123 }, { id: 456 }, { id: 8981 }];
        const personalDataTypeCollection: IPersonalDataType[] = [{ id: 123 }];
        expectedResult = service.addPersonalDataTypeToCollectionIfMissing(personalDataTypeCollection, ...personalDataTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const personalDataType: IPersonalDataType = { id: 123 };
        const personalDataType2: IPersonalDataType = { id: 456 };
        expectedResult = service.addPersonalDataTypeToCollectionIfMissing([], personalDataType, personalDataType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personalDataType);
        expect(expectedResult).toContain(personalDataType2);
      });

      it('should accept null and undefined values', () => {
        const personalDataType: IPersonalDataType = { id: 123 };
        expectedResult = service.addPersonalDataTypeToCollectionIfMissing([], null, personalDataType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personalDataType);
      });

      it('should return initial array if no PersonalDataType is added', () => {
        const personalDataTypeCollection: IPersonalDataType[] = [{ id: 123 }];
        expectedResult = service.addPersonalDataTypeToCollectionIfMissing(personalDataTypeCollection, undefined, null);
        expect(expectedResult).toEqual(personalDataTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
