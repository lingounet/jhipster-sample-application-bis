import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIcrfStatus, IcrfStatus } from '../icrf-status.model';

import { IcrfStatusService } from './icrf-status.service';

describe('IcrfStatus Service', () => {
  let service: IcrfStatusService;
  let httpMock: HttpTestingController;
  let elemDefault: IIcrfStatus;
  let expectedResult: IIcrfStatus | IIcrfStatus[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IcrfStatusService);
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

    it('should create a IcrfStatus', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new IcrfStatus()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IcrfStatus', () => {
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

    it('should partial update a IcrfStatus', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new IcrfStatus()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IcrfStatus', () => {
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

    it('should delete a IcrfStatus', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addIcrfStatusToCollectionIfMissing', () => {
      it('should add a IcrfStatus to an empty array', () => {
        const icrfStatus: IIcrfStatus = { id: 123 };
        expectedResult = service.addIcrfStatusToCollectionIfMissing([], icrfStatus);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(icrfStatus);
      });

      it('should not add a IcrfStatus to an array that contains it', () => {
        const icrfStatus: IIcrfStatus = { id: 123 };
        const icrfStatusCollection: IIcrfStatus[] = [
          {
            ...icrfStatus,
          },
          { id: 456 },
        ];
        expectedResult = service.addIcrfStatusToCollectionIfMissing(icrfStatusCollection, icrfStatus);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IcrfStatus to an array that doesn't contain it", () => {
        const icrfStatus: IIcrfStatus = { id: 123 };
        const icrfStatusCollection: IIcrfStatus[] = [{ id: 456 }];
        expectedResult = service.addIcrfStatusToCollectionIfMissing(icrfStatusCollection, icrfStatus);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(icrfStatus);
      });

      it('should add only unique IcrfStatus to an array', () => {
        const icrfStatusArray: IIcrfStatus[] = [{ id: 123 }, { id: 456 }, { id: 17075 }];
        const icrfStatusCollection: IIcrfStatus[] = [{ id: 123 }];
        expectedResult = service.addIcrfStatusToCollectionIfMissing(icrfStatusCollection, ...icrfStatusArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const icrfStatus: IIcrfStatus = { id: 123 };
        const icrfStatus2: IIcrfStatus = { id: 456 };
        expectedResult = service.addIcrfStatusToCollectionIfMissing([], icrfStatus, icrfStatus2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(icrfStatus);
        expect(expectedResult).toContain(icrfStatus2);
      });

      it('should accept null and undefined values', () => {
        const icrfStatus: IIcrfStatus = { id: 123 };
        expectedResult = service.addIcrfStatusToCollectionIfMissing([], null, icrfStatus, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(icrfStatus);
      });

      it('should return initial array if no IcrfStatus is added', () => {
        const icrfStatusCollection: IIcrfStatus[] = [{ id: 123 }];
        expectedResult = service.addIcrfStatusToCollectionIfMissing(icrfStatusCollection, undefined, null);
        expect(expectedResult).toEqual(icrfStatusCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
