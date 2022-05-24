import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHostingPlatform, HostingPlatform } from '../hosting-platform.model';

import { HostingPlatformService } from './hosting-platform.service';

describe('HostingPlatform Service', () => {
  let service: HostingPlatformService;
  let httpMock: HttpTestingController;
  let elemDefault: IHostingPlatform;
  let expectedResult: IHostingPlatform | IHostingPlatform[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HostingPlatformService);
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

    it('should create a HostingPlatform', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new HostingPlatform()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HostingPlatform', () => {
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

    it('should partial update a HostingPlatform', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new HostingPlatform()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HostingPlatform', () => {
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

    it('should delete a HostingPlatform', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addHostingPlatformToCollectionIfMissing', () => {
      it('should add a HostingPlatform to an empty array', () => {
        const hostingPlatform: IHostingPlatform = { id: 123 };
        expectedResult = service.addHostingPlatformToCollectionIfMissing([], hostingPlatform);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hostingPlatform);
      });

      it('should not add a HostingPlatform to an array that contains it', () => {
        const hostingPlatform: IHostingPlatform = { id: 123 };
        const hostingPlatformCollection: IHostingPlatform[] = [
          {
            ...hostingPlatform,
          },
          { id: 456 },
        ];
        expectedResult = service.addHostingPlatformToCollectionIfMissing(hostingPlatformCollection, hostingPlatform);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HostingPlatform to an array that doesn't contain it", () => {
        const hostingPlatform: IHostingPlatform = { id: 123 };
        const hostingPlatformCollection: IHostingPlatform[] = [{ id: 456 }];
        expectedResult = service.addHostingPlatformToCollectionIfMissing(hostingPlatformCollection, hostingPlatform);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hostingPlatform);
      });

      it('should add only unique HostingPlatform to an array', () => {
        const hostingPlatformArray: IHostingPlatform[] = [{ id: 123 }, { id: 456 }, { id: 54315 }];
        const hostingPlatformCollection: IHostingPlatform[] = [{ id: 123 }];
        expectedResult = service.addHostingPlatformToCollectionIfMissing(hostingPlatformCollection, ...hostingPlatformArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const hostingPlatform: IHostingPlatform = { id: 123 };
        const hostingPlatform2: IHostingPlatform = { id: 456 };
        expectedResult = service.addHostingPlatformToCollectionIfMissing([], hostingPlatform, hostingPlatform2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hostingPlatform);
        expect(expectedResult).toContain(hostingPlatform2);
      });

      it('should accept null and undefined values', () => {
        const hostingPlatform: IHostingPlatform = { id: 123 };
        expectedResult = service.addHostingPlatformToCollectionIfMissing([], null, hostingPlatform, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hostingPlatform);
      });

      it('should return initial array if no HostingPlatform is added', () => {
        const hostingPlatformCollection: IHostingPlatform[] = [{ id: 123 }];
        expectedResult = service.addHostingPlatformToCollectionIfMissing(hostingPlatformCollection, undefined, null);
        expect(expectedResult).toEqual(hostingPlatformCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
