import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISensitiveDataType, SensitiveDataType } from '../sensitive-data-type.model';

import { SensitiveDataTypeService } from './sensitive-data-type.service';

describe('SensitiveDataType Service', () => {
  let service: SensitiveDataTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ISensitiveDataType;
  let expectedResult: ISensitiveDataType | ISensitiveDataType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SensitiveDataTypeService);
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

    it('should create a SensitiveDataType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SensitiveDataType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SensitiveDataType', () => {
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

    it('should partial update a SensitiveDataType', () => {
      const patchObject = Object.assign({}, new SensitiveDataType());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SensitiveDataType', () => {
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

    it('should delete a SensitiveDataType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSensitiveDataTypeToCollectionIfMissing', () => {
      it('should add a SensitiveDataType to an empty array', () => {
        const sensitiveDataType: ISensitiveDataType = { id: 123 };
        expectedResult = service.addSensitiveDataTypeToCollectionIfMissing([], sensitiveDataType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sensitiveDataType);
      });

      it('should not add a SensitiveDataType to an array that contains it', () => {
        const sensitiveDataType: ISensitiveDataType = { id: 123 };
        const sensitiveDataTypeCollection: ISensitiveDataType[] = [
          {
            ...sensitiveDataType,
          },
          { id: 456 },
        ];
        expectedResult = service.addSensitiveDataTypeToCollectionIfMissing(sensitiveDataTypeCollection, sensitiveDataType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SensitiveDataType to an array that doesn't contain it", () => {
        const sensitiveDataType: ISensitiveDataType = { id: 123 };
        const sensitiveDataTypeCollection: ISensitiveDataType[] = [{ id: 456 }];
        expectedResult = service.addSensitiveDataTypeToCollectionIfMissing(sensitiveDataTypeCollection, sensitiveDataType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sensitiveDataType);
      });

      it('should add only unique SensitiveDataType to an array', () => {
        const sensitiveDataTypeArray: ISensitiveDataType[] = [{ id: 123 }, { id: 456 }, { id: 25040 }];
        const sensitiveDataTypeCollection: ISensitiveDataType[] = [{ id: 123 }];
        expectedResult = service.addSensitiveDataTypeToCollectionIfMissing(sensitiveDataTypeCollection, ...sensitiveDataTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sensitiveDataType: ISensitiveDataType = { id: 123 };
        const sensitiveDataType2: ISensitiveDataType = { id: 456 };
        expectedResult = service.addSensitiveDataTypeToCollectionIfMissing([], sensitiveDataType, sensitiveDataType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sensitiveDataType);
        expect(expectedResult).toContain(sensitiveDataType2);
      });

      it('should accept null and undefined values', () => {
        const sensitiveDataType: ISensitiveDataType = { id: 123 };
        expectedResult = service.addSensitiveDataTypeToCollectionIfMissing([], null, sensitiveDataType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sensitiveDataType);
      });

      it('should return initial array if no SensitiveDataType is added', () => {
        const sensitiveDataTypeCollection: ISensitiveDataType[] = [{ id: 123 }];
        expectedResult = service.addSensitiveDataTypeToCollectionIfMissing(sensitiveDataTypeCollection, undefined, null);
        expect(expectedResult).toEqual(sensitiveDataTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
