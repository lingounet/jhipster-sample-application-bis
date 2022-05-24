import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Process } from 'app/entities/enumerations/process.model';
import { ISecurityInterview, SecurityInterview } from '../security-interview.model';

import { SecurityInterviewService } from './security-interview.service';

describe('SecurityInterview Service', () => {
  let service: SecurityInterviewService;
  let httpMock: HttpTestingController;
  let elemDefault: ISecurityInterview;
  let expectedResult: ISecurityInterview | ISecurityInterview[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SecurityInterviewService);
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

    it('should create a SecurityInterview', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SecurityInterview()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SecurityInterview', () => {
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

    it('should partial update a SecurityInterview', () => {
      const patchObject = Object.assign(
        {
          so: 'BBBBBB',
          process: 'BBBBBB',
        },
        new SecurityInterview()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SecurityInterview', () => {
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

    it('should delete a SecurityInterview', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSecurityInterviewToCollectionIfMissing', () => {
      it('should add a SecurityInterview to an empty array', () => {
        const securityInterview: ISecurityInterview = { id: 123 };
        expectedResult = service.addSecurityInterviewToCollectionIfMissing([], securityInterview);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(securityInterview);
      });

      it('should not add a SecurityInterview to an array that contains it', () => {
        const securityInterview: ISecurityInterview = { id: 123 };
        const securityInterviewCollection: ISecurityInterview[] = [
          {
            ...securityInterview,
          },
          { id: 456 },
        ];
        expectedResult = service.addSecurityInterviewToCollectionIfMissing(securityInterviewCollection, securityInterview);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SecurityInterview to an array that doesn't contain it", () => {
        const securityInterview: ISecurityInterview = { id: 123 };
        const securityInterviewCollection: ISecurityInterview[] = [{ id: 456 }];
        expectedResult = service.addSecurityInterviewToCollectionIfMissing(securityInterviewCollection, securityInterview);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(securityInterview);
      });

      it('should add only unique SecurityInterview to an array', () => {
        const securityInterviewArray: ISecurityInterview[] = [{ id: 123 }, { id: 456 }, { id: 72545 }];
        const securityInterviewCollection: ISecurityInterview[] = [{ id: 123 }];
        expectedResult = service.addSecurityInterviewToCollectionIfMissing(securityInterviewCollection, ...securityInterviewArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const securityInterview: ISecurityInterview = { id: 123 };
        const securityInterview2: ISecurityInterview = { id: 456 };
        expectedResult = service.addSecurityInterviewToCollectionIfMissing([], securityInterview, securityInterview2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(securityInterview);
        expect(expectedResult).toContain(securityInterview2);
      });

      it('should accept null and undefined values', () => {
        const securityInterview: ISecurityInterview = { id: 123 };
        expectedResult = service.addSecurityInterviewToCollectionIfMissing([], null, securityInterview, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(securityInterview);
      });

      it('should return initial array if no SecurityInterview is added', () => {
        const securityInterviewCollection: ISecurityInterview[] = [{ id: 123 }];
        expectedResult = service.addSecurityInterviewToCollectionIfMissing(securityInterviewCollection, undefined, null);
        expect(expectedResult).toEqual(securityInterviewCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
