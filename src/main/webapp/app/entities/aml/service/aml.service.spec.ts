import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAml, Aml } from '../aml.model';

import { AmlService } from './aml.service';

describe('Aml Service', () => {
  let service: AmlService;
  let httpMock: HttpTestingController;
  let elemDefault: IAml;
  let expectedResult: IAml | IAml[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AmlService);
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

    it('should create a Aml', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Aml()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Aml', () => {
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

    it('should partial update a Aml', () => {
      const patchObject = Object.assign({}, new Aml());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Aml', () => {
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

    it('should delete a Aml', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAmlToCollectionIfMissing', () => {
      it('should add a Aml to an empty array', () => {
        const aml: IAml = { id: 123 };
        expectedResult = service.addAmlToCollectionIfMissing([], aml);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aml);
      });

      it('should not add a Aml to an array that contains it', () => {
        const aml: IAml = { id: 123 };
        const amlCollection: IAml[] = [
          {
            ...aml,
          },
          { id: 456 },
        ];
        expectedResult = service.addAmlToCollectionIfMissing(amlCollection, aml);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Aml to an array that doesn't contain it", () => {
        const aml: IAml = { id: 123 };
        const amlCollection: IAml[] = [{ id: 456 }];
        expectedResult = service.addAmlToCollectionIfMissing(amlCollection, aml);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aml);
      });

      it('should add only unique Aml to an array', () => {
        const amlArray: IAml[] = [{ id: 123 }, { id: 456 }, { id: 20133 }];
        const amlCollection: IAml[] = [{ id: 123 }];
        expectedResult = service.addAmlToCollectionIfMissing(amlCollection, ...amlArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aml: IAml = { id: 123 };
        const aml2: IAml = { id: 456 };
        expectedResult = service.addAmlToCollectionIfMissing([], aml, aml2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aml);
        expect(expectedResult).toContain(aml2);
      });

      it('should accept null and undefined values', () => {
        const aml: IAml = { id: 123 };
        expectedResult = service.addAmlToCollectionIfMissing([], null, aml, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aml);
      });

      it('should return initial array if no Aml is added', () => {
        const amlCollection: IAml[] = [{ id: 123 }];
        expectedResult = service.addAmlToCollectionIfMissing(amlCollection, undefined, null);
        expect(expectedResult).toEqual(amlCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
