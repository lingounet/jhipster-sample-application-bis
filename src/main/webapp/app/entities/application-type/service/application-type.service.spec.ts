import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IApplicationType, ApplicationType } from '../application-type.model';

import { ApplicationTypeService } from './application-type.service';

describe('ApplicationType Service', () => {
  let service: ApplicationTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IApplicationType;
  let expectedResult: IApplicationType | IApplicationType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ApplicationTypeService);
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

    it('should create a ApplicationType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ApplicationType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ApplicationType', () => {
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

    it('should partial update a ApplicationType', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new ApplicationType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ApplicationType', () => {
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

    it('should delete a ApplicationType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addApplicationTypeToCollectionIfMissing', () => {
      it('should add a ApplicationType to an empty array', () => {
        const applicationType: IApplicationType = { id: 123 };
        expectedResult = service.addApplicationTypeToCollectionIfMissing([], applicationType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(applicationType);
      });

      it('should not add a ApplicationType to an array that contains it', () => {
        const applicationType: IApplicationType = { id: 123 };
        const applicationTypeCollection: IApplicationType[] = [
          {
            ...applicationType,
          },
          { id: 456 },
        ];
        expectedResult = service.addApplicationTypeToCollectionIfMissing(applicationTypeCollection, applicationType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ApplicationType to an array that doesn't contain it", () => {
        const applicationType: IApplicationType = { id: 123 };
        const applicationTypeCollection: IApplicationType[] = [{ id: 456 }];
        expectedResult = service.addApplicationTypeToCollectionIfMissing(applicationTypeCollection, applicationType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(applicationType);
      });

      it('should add only unique ApplicationType to an array', () => {
        const applicationTypeArray: IApplicationType[] = [{ id: 123 }, { id: 456 }, { id: 31016 }];
        const applicationTypeCollection: IApplicationType[] = [{ id: 123 }];
        expectedResult = service.addApplicationTypeToCollectionIfMissing(applicationTypeCollection, ...applicationTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const applicationType: IApplicationType = { id: 123 };
        const applicationType2: IApplicationType = { id: 456 };
        expectedResult = service.addApplicationTypeToCollectionIfMissing([], applicationType, applicationType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(applicationType);
        expect(expectedResult).toContain(applicationType2);
      });

      it('should accept null and undefined values', () => {
        const applicationType: IApplicationType = { id: 123 };
        expectedResult = service.addApplicationTypeToCollectionIfMissing([], null, applicationType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(applicationType);
      });

      it('should return initial array if no ApplicationType is added', () => {
        const applicationTypeCollection: IApplicationType[] = [{ id: 123 }];
        expectedResult = service.addApplicationTypeToCollectionIfMissing(applicationTypeCollection, undefined, null);
        expect(expectedResult).toEqual(applicationTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
