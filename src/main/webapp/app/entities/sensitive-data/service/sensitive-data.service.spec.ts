import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISensitiveData, SensitiveData } from '../sensitive-data.model';

import { SensitiveDataService } from './sensitive-data.service';

describe('SensitiveData Service', () => {
  let service: SensitiveDataService;
  let httpMock: HttpTestingController;
  let elemDefault: ISensitiveData;
  let expectedResult: ISensitiveData | ISensitiveData[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SensitiveDataService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      date: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a SensitiveData', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.create(new SensitiveData()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SensitiveData', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SensitiveData', () => {
      const patchObject = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        new SensitiveData()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SensitiveData', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a SensitiveData', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSensitiveDataToCollectionIfMissing', () => {
      it('should add a SensitiveData to an empty array', () => {
        const sensitiveData: ISensitiveData = { id: 123 };
        expectedResult = service.addSensitiveDataToCollectionIfMissing([], sensitiveData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sensitiveData);
      });

      it('should not add a SensitiveData to an array that contains it', () => {
        const sensitiveData: ISensitiveData = { id: 123 };
        const sensitiveDataCollection: ISensitiveData[] = [
          {
            ...sensitiveData,
          },
          { id: 456 },
        ];
        expectedResult = service.addSensitiveDataToCollectionIfMissing(sensitiveDataCollection, sensitiveData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SensitiveData to an array that doesn't contain it", () => {
        const sensitiveData: ISensitiveData = { id: 123 };
        const sensitiveDataCollection: ISensitiveData[] = [{ id: 456 }];
        expectedResult = service.addSensitiveDataToCollectionIfMissing(sensitiveDataCollection, sensitiveData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sensitiveData);
      });

      it('should add only unique SensitiveData to an array', () => {
        const sensitiveDataArray: ISensitiveData[] = [{ id: 123 }, { id: 456 }, { id: 29549 }];
        const sensitiveDataCollection: ISensitiveData[] = [{ id: 123 }];
        expectedResult = service.addSensitiveDataToCollectionIfMissing(sensitiveDataCollection, ...sensitiveDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sensitiveData: ISensitiveData = { id: 123 };
        const sensitiveData2: ISensitiveData = { id: 456 };
        expectedResult = service.addSensitiveDataToCollectionIfMissing([], sensitiveData, sensitiveData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sensitiveData);
        expect(expectedResult).toContain(sensitiveData2);
      });

      it('should accept null and undefined values', () => {
        const sensitiveData: ISensitiveData = { id: 123 };
        expectedResult = service.addSensitiveDataToCollectionIfMissing([], null, sensitiveData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sensitiveData);
      });

      it('should return initial array if no SensitiveData is added', () => {
        const sensitiveDataCollection: ISensitiveData[] = [{ id: 123 }];
        expectedResult = service.addSensitiveDataToCollectionIfMissing(sensitiveDataCollection, undefined, null);
        expect(expectedResult).toEqual(sensitiveDataCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
