import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IHosting, Hosting } from '../hosting.model';

import { HostingService } from './hosting.service';

describe('Hosting Service', () => {
  let service: HostingService;
  let httpMock: HttpTestingController;
  let elemDefault: IHosting;
  let expectedResult: IHosting | IHosting[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HostingService);
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

    it('should create a Hosting', () => {
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

      service.create(new Hosting()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Hosting', () => {
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

    it('should partial update a Hosting', () => {
      const patchObject = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        new Hosting()
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

    it('should return a list of Hosting', () => {
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

    it('should delete a Hosting', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addHostingToCollectionIfMissing', () => {
      it('should add a Hosting to an empty array', () => {
        const hosting: IHosting = { id: 123 };
        expectedResult = service.addHostingToCollectionIfMissing([], hosting);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hosting);
      });

      it('should not add a Hosting to an array that contains it', () => {
        const hosting: IHosting = { id: 123 };
        const hostingCollection: IHosting[] = [
          {
            ...hosting,
          },
          { id: 456 },
        ];
        expectedResult = service.addHostingToCollectionIfMissing(hostingCollection, hosting);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Hosting to an array that doesn't contain it", () => {
        const hosting: IHosting = { id: 123 };
        const hostingCollection: IHosting[] = [{ id: 456 }];
        expectedResult = service.addHostingToCollectionIfMissing(hostingCollection, hosting);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hosting);
      });

      it('should add only unique Hosting to an array', () => {
        const hostingArray: IHosting[] = [{ id: 123 }, { id: 456 }, { id: 59980 }];
        const hostingCollection: IHosting[] = [{ id: 123 }];
        expectedResult = service.addHostingToCollectionIfMissing(hostingCollection, ...hostingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const hosting: IHosting = { id: 123 };
        const hosting2: IHosting = { id: 456 };
        expectedResult = service.addHostingToCollectionIfMissing([], hosting, hosting2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hosting);
        expect(expectedResult).toContain(hosting2);
      });

      it('should accept null and undefined values', () => {
        const hosting: IHosting = { id: 123 };
        expectedResult = service.addHostingToCollectionIfMissing([], null, hosting, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hosting);
      });

      it('should return initial array if no Hosting is added', () => {
        const hostingCollection: IHosting[] = [{ id: 123 }];
        expectedResult = service.addHostingToCollectionIfMissing(hostingCollection, undefined, null);
        expect(expectedResult).toEqual(hostingCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
