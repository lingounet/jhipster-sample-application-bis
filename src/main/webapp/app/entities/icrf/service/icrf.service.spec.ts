import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIcrf, Icrf } from '../icrf.model';

import { IcrfService } from './icrf.service';

describe('Icrf Service', () => {
  let service: IcrfService;
  let httpMock: HttpTestingController;
  let elemDefault: IIcrf;
  let expectedResult: IIcrf | IIcrf[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IcrfService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      code: 'AAAAAAA',
      description: 'AAAAAAA',
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

    it('should create a Icrf', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Icrf()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Icrf', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          code: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Icrf', () => {
      const patchObject = Object.assign({}, new Icrf());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Icrf', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          code: 'BBBBBB',
          description: 'BBBBBB',
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

    it('should delete a Icrf', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addIcrfToCollectionIfMissing', () => {
      it('should add a Icrf to an empty array', () => {
        const icrf: IIcrf = { id: 123 };
        expectedResult = service.addIcrfToCollectionIfMissing([], icrf);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(icrf);
      });

      it('should not add a Icrf to an array that contains it', () => {
        const icrf: IIcrf = { id: 123 };
        const icrfCollection: IIcrf[] = [
          {
            ...icrf,
          },
          { id: 456 },
        ];
        expectedResult = service.addIcrfToCollectionIfMissing(icrfCollection, icrf);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Icrf to an array that doesn't contain it", () => {
        const icrf: IIcrf = { id: 123 };
        const icrfCollection: IIcrf[] = [{ id: 456 }];
        expectedResult = service.addIcrfToCollectionIfMissing(icrfCollection, icrf);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(icrf);
      });

      it('should add only unique Icrf to an array', () => {
        const icrfArray: IIcrf[] = [{ id: 123 }, { id: 456 }, { id: 46871 }];
        const icrfCollection: IIcrf[] = [{ id: 123 }];
        expectedResult = service.addIcrfToCollectionIfMissing(icrfCollection, ...icrfArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const icrf: IIcrf = { id: 123 };
        const icrf2: IIcrf = { id: 456 };
        expectedResult = service.addIcrfToCollectionIfMissing([], icrf, icrf2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(icrf);
        expect(expectedResult).toContain(icrf2);
      });

      it('should accept null and undefined values', () => {
        const icrf: IIcrf = { id: 123 };
        expectedResult = service.addIcrfToCollectionIfMissing([], null, icrf, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(icrf);
      });

      it('should return initial array if no Icrf is added', () => {
        const icrfCollection: IIcrf[] = [{ id: 123 }];
        expectedResult = service.addIcrfToCollectionIfMissing(icrfCollection, undefined, null);
        expect(expectedResult).toEqual(icrfCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
