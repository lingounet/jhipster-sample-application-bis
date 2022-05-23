import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Status } from 'app/entities/enumerations/status.model';
import { IPsat, Psat } from '../psat.model';

import { PsatService } from './psat.service';

describe('Psat Service', () => {
  let service: PsatService;
  let httpMock: HttpTestingController;
  let elemDefault: IPsat;
  let expectedResult: IPsat | IPsat[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PsatService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      amlId: 'AAAAAAA',
      owner: 'AAAAAAA',
      status: Status.DRAFT,
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

    it('should create a Psat', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Psat()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Psat', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          amlId: 'BBBBBB',
          owner: 'BBBBBB',
          status: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Psat', () => {
      const patchObject = Object.assign(
        {
          amlId: 'BBBBBB',
          owner: 'BBBBBB',
        },
        new Psat()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Psat', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          amlId: 'BBBBBB',
          owner: 'BBBBBB',
          status: 'BBBBBB',
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

    it('should delete a Psat', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPsatToCollectionIfMissing', () => {
      it('should add a Psat to an empty array', () => {
        const psat: IPsat = { id: 123 };
        expectedResult = service.addPsatToCollectionIfMissing([], psat);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(psat);
      });

      it('should not add a Psat to an array that contains it', () => {
        const psat: IPsat = { id: 123 };
        const psatCollection: IPsat[] = [
          {
            ...psat,
          },
          { id: 456 },
        ];
        expectedResult = service.addPsatToCollectionIfMissing(psatCollection, psat);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Psat to an array that doesn't contain it", () => {
        const psat: IPsat = { id: 123 };
        const psatCollection: IPsat[] = [{ id: 456 }];
        expectedResult = service.addPsatToCollectionIfMissing(psatCollection, psat);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(psat);
      });

      it('should add only unique Psat to an array', () => {
        const psatArray: IPsat[] = [{ id: 123 }, { id: 456 }, { id: 76880 }];
        const psatCollection: IPsat[] = [{ id: 123 }];
        expectedResult = service.addPsatToCollectionIfMissing(psatCollection, ...psatArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const psat: IPsat = { id: 123 };
        const psat2: IPsat = { id: 456 };
        expectedResult = service.addPsatToCollectionIfMissing([], psat, psat2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(psat);
        expect(expectedResult).toContain(psat2);
      });

      it('should accept null and undefined values', () => {
        const psat: IPsat = { id: 123 };
        expectedResult = service.addPsatToCollectionIfMissing([], null, psat, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(psat);
      });

      it('should return initial array if no Psat is added', () => {
        const psatCollection: IPsat[] = [{ id: 123 }];
        expectedResult = service.addPsatToCollectionIfMissing(psatCollection, undefined, null);
        expect(expectedResult).toEqual(psatCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
