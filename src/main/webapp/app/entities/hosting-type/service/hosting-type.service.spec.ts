import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHostingType, HostingType } from '../hosting-type.model';

import { HostingTypeService } from './hosting-type.service';

describe('HostingType Service', () => {
  let service: HostingTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IHostingType;
  let expectedResult: IHostingType | IHostingType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HostingTypeService);
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

    it('should create a HostingType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new HostingType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HostingType', () => {
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

    it('should partial update a HostingType', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new HostingType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HostingType', () => {
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

    it('should delete a HostingType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addHostingTypeToCollectionIfMissing', () => {
      it('should add a HostingType to an empty array', () => {
        const hostingType: IHostingType = { id: 123 };
        expectedResult = service.addHostingTypeToCollectionIfMissing([], hostingType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hostingType);
      });

      it('should not add a HostingType to an array that contains it', () => {
        const hostingType: IHostingType = { id: 123 };
        const hostingTypeCollection: IHostingType[] = [
          {
            ...hostingType,
          },
          { id: 456 },
        ];
        expectedResult = service.addHostingTypeToCollectionIfMissing(hostingTypeCollection, hostingType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HostingType to an array that doesn't contain it", () => {
        const hostingType: IHostingType = { id: 123 };
        const hostingTypeCollection: IHostingType[] = [{ id: 456 }];
        expectedResult = service.addHostingTypeToCollectionIfMissing(hostingTypeCollection, hostingType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hostingType);
      });

      it('should add only unique HostingType to an array', () => {
        const hostingTypeArray: IHostingType[] = [{ id: 123 }, { id: 456 }, { id: 73849 }];
        const hostingTypeCollection: IHostingType[] = [{ id: 123 }];
        expectedResult = service.addHostingTypeToCollectionIfMissing(hostingTypeCollection, ...hostingTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const hostingType: IHostingType = { id: 123 };
        const hostingType2: IHostingType = { id: 456 };
        expectedResult = service.addHostingTypeToCollectionIfMissing([], hostingType, hostingType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hostingType);
        expect(expectedResult).toContain(hostingType2);
      });

      it('should accept null and undefined values', () => {
        const hostingType: IHostingType = { id: 123 };
        expectedResult = service.addHostingTypeToCollectionIfMissing([], null, hostingType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hostingType);
      });

      it('should return initial array if no HostingType is added', () => {
        const hostingTypeCollection: IHostingType[] = [{ id: 123 }];
        expectedResult = service.addHostingTypeToCollectionIfMissing(hostingTypeCollection, undefined, null);
        expect(expectedResult).toEqual(hostingTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
