import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Process } from 'app/entities/enumerations/process.model';
import { IIdentity, Identity } from '../identity.model';

import { IdentityService } from './identity.service';

describe('Identity Service', () => {
  let service: IdentityService;
  let httpMock: HttpTestingController;
  let elemDefault: IIdentity;
  let expectedResult: IIdentity | IIdentity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IdentityService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      applicationName: 'AAAAAAA',
      so: 'AAAAAAA',
      process: Process.LIGHT,
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

    it('should create a Identity', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Identity()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Identity', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          applicationName: 'BBBBBB',
          so: 'BBBBBB',
          process: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Identity', () => {
      const patchObject = Object.assign({}, new Identity());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Identity', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          applicationName: 'BBBBBB',
          so: 'BBBBBB',
          process: 'BBBBBB',
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

    it('should delete a Identity', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addIdentityToCollectionIfMissing', () => {
      it('should add a Identity to an empty array', () => {
        const identity: IIdentity = { id: 123 };
        expectedResult = service.addIdentityToCollectionIfMissing([], identity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(identity);
      });

      it('should not add a Identity to an array that contains it', () => {
        const identity: IIdentity = { id: 123 };
        const identityCollection: IIdentity[] = [
          {
            ...identity,
          },
          { id: 456 },
        ];
        expectedResult = service.addIdentityToCollectionIfMissing(identityCollection, identity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Identity to an array that doesn't contain it", () => {
        const identity: IIdentity = { id: 123 };
        const identityCollection: IIdentity[] = [{ id: 456 }];
        expectedResult = service.addIdentityToCollectionIfMissing(identityCollection, identity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(identity);
      });

      it('should add only unique Identity to an array', () => {
        const identityArray: IIdentity[] = [{ id: 123 }, { id: 456 }, { id: 75607 }];
        const identityCollection: IIdentity[] = [{ id: 123 }];
        expectedResult = service.addIdentityToCollectionIfMissing(identityCollection, ...identityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const identity: IIdentity = { id: 123 };
        const identity2: IIdentity = { id: 456 };
        expectedResult = service.addIdentityToCollectionIfMissing([], identity, identity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(identity);
        expect(expectedResult).toContain(identity2);
      });

      it('should accept null and undefined values', () => {
        const identity: IIdentity = { id: 123 };
        expectedResult = service.addIdentityToCollectionIfMissing([], null, identity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(identity);
      });

      it('should return initial array if no Identity is added', () => {
        const identityCollection: IIdentity[] = [{ id: 123 }];
        expectedResult = service.addIdentityToCollectionIfMissing(identityCollection, undefined, null);
        expect(expectedResult).toEqual(identityCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
