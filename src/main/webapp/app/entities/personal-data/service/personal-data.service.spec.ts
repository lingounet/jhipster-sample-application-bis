import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPersonalData, PersonalData } from '../personal-data.model';

import { PersonalDataService } from './personal-data.service';

describe('PersonalData Service', () => {
  let service: PersonalDataService;
  let httpMock: HttpTestingController;
  let elemDefault: IPersonalData;
  let expectedResult: IPersonalData | IPersonalData[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PersonalDataService);
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

    it('should create a PersonalData', () => {
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

      service.create(new PersonalData()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PersonalData', () => {
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

    it('should partial update a PersonalData', () => {
      const patchObject = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        new PersonalData()
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

    it('should return a list of PersonalData', () => {
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

    it('should delete a PersonalData', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPersonalDataToCollectionIfMissing', () => {
      it('should add a PersonalData to an empty array', () => {
        const personalData: IPersonalData = { id: 123 };
        expectedResult = service.addPersonalDataToCollectionIfMissing([], personalData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personalData);
      });

      it('should not add a PersonalData to an array that contains it', () => {
        const personalData: IPersonalData = { id: 123 };
        const personalDataCollection: IPersonalData[] = [
          {
            ...personalData,
          },
          { id: 456 },
        ];
        expectedResult = service.addPersonalDataToCollectionIfMissing(personalDataCollection, personalData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PersonalData to an array that doesn't contain it", () => {
        const personalData: IPersonalData = { id: 123 };
        const personalDataCollection: IPersonalData[] = [{ id: 456 }];
        expectedResult = service.addPersonalDataToCollectionIfMissing(personalDataCollection, personalData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personalData);
      });

      it('should add only unique PersonalData to an array', () => {
        const personalDataArray: IPersonalData[] = [{ id: 123 }, { id: 456 }, { id: 61181 }];
        const personalDataCollection: IPersonalData[] = [{ id: 123 }];
        expectedResult = service.addPersonalDataToCollectionIfMissing(personalDataCollection, ...personalDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const personalData: IPersonalData = { id: 123 };
        const personalData2: IPersonalData = { id: 456 };
        expectedResult = service.addPersonalDataToCollectionIfMissing([], personalData, personalData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personalData);
        expect(expectedResult).toContain(personalData2);
      });

      it('should accept null and undefined values', () => {
        const personalData: IPersonalData = { id: 123 };
        expectedResult = service.addPersonalDataToCollectionIfMissing([], null, personalData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personalData);
      });

      it('should return initial array if no PersonalData is added', () => {
        const personalDataCollection: IPersonalData[] = [{ id: 123 }];
        expectedResult = service.addPersonalDataToCollectionIfMissing(personalDataCollection, undefined, null);
        expect(expectedResult).toEqual(personalDataCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
