import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIcrfAssessment, IcrfAssessment } from '../icrf-assessment.model';

import { IcrfAssessmentService } from './icrf-assessment.service';

describe('IcrfAssessment Service', () => {
  let service: IcrfAssessmentService;
  let httpMock: HttpTestingController;
  let elemDefault: IIcrfAssessment;
  let expectedResult: IIcrfAssessment | IIcrfAssessment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IcrfAssessmentService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      code: 'AAAAAAA',
      description: 'AAAAAAA',
      status: false,
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

    it('should create a IcrfAssessment', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new IcrfAssessment()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IcrfAssessment', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          code: 'BBBBBB',
          description: 'BBBBBB',
          status: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IcrfAssessment', () => {
      const patchObject = Object.assign({}, new IcrfAssessment());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IcrfAssessment', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          code: 'BBBBBB',
          description: 'BBBBBB',
          status: true,
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

    it('should delete a IcrfAssessment', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addIcrfAssessmentToCollectionIfMissing', () => {
      it('should add a IcrfAssessment to an empty array', () => {
        const icrfAssessment: IIcrfAssessment = { id: 123 };
        expectedResult = service.addIcrfAssessmentToCollectionIfMissing([], icrfAssessment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(icrfAssessment);
      });

      it('should not add a IcrfAssessment to an array that contains it', () => {
        const icrfAssessment: IIcrfAssessment = { id: 123 };
        const icrfAssessmentCollection: IIcrfAssessment[] = [
          {
            ...icrfAssessment,
          },
          { id: 456 },
        ];
        expectedResult = service.addIcrfAssessmentToCollectionIfMissing(icrfAssessmentCollection, icrfAssessment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IcrfAssessment to an array that doesn't contain it", () => {
        const icrfAssessment: IIcrfAssessment = { id: 123 };
        const icrfAssessmentCollection: IIcrfAssessment[] = [{ id: 456 }];
        expectedResult = service.addIcrfAssessmentToCollectionIfMissing(icrfAssessmentCollection, icrfAssessment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(icrfAssessment);
      });

      it('should add only unique IcrfAssessment to an array', () => {
        const icrfAssessmentArray: IIcrfAssessment[] = [{ id: 123 }, { id: 456 }, { id: 55165 }];
        const icrfAssessmentCollection: IIcrfAssessment[] = [{ id: 123 }];
        expectedResult = service.addIcrfAssessmentToCollectionIfMissing(icrfAssessmentCollection, ...icrfAssessmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const icrfAssessment: IIcrfAssessment = { id: 123 };
        const icrfAssessment2: IIcrfAssessment = { id: 456 };
        expectedResult = service.addIcrfAssessmentToCollectionIfMissing([], icrfAssessment, icrfAssessment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(icrfAssessment);
        expect(expectedResult).toContain(icrfAssessment2);
      });

      it('should accept null and undefined values', () => {
        const icrfAssessment: IIcrfAssessment = { id: 123 };
        expectedResult = service.addIcrfAssessmentToCollectionIfMissing([], null, icrfAssessment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(icrfAssessment);
      });

      it('should return initial array if no IcrfAssessment is added', () => {
        const icrfAssessmentCollection: IIcrfAssessment[] = [{ id: 123 }];
        expectedResult = service.addIcrfAssessmentToCollectionIfMissing(icrfAssessmentCollection, undefined, null);
        expect(expectedResult).toEqual(icrfAssessmentCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
