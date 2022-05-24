import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IComplementaryQuestion, ComplementaryQuestion } from '../complementary-question.model';

import { ComplementaryQuestionService } from './complementary-question.service';

describe('ComplementaryQuestion Service', () => {
  let service: ComplementaryQuestionService;
  let httpMock: HttpTestingController;
  let elemDefault: IComplementaryQuestion;
  let expectedResult: IComplementaryQuestion | IComplementaryQuestion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ComplementaryQuestionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      internet: false,
      development: false,
      configuration: false,
      cloud: false,
      internal: false,
      partner: false,
      users: 0,
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

    it('should create a ComplementaryQuestion', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ComplementaryQuestion()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ComplementaryQuestion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          internet: true,
          development: true,
          configuration: true,
          cloud: true,
          internal: true,
          partner: true,
          users: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ComplementaryQuestion', () => {
      const patchObject = Object.assign(
        {
          internet: true,
          development: true,
          cloud: true,
          partner: true,
        },
        new ComplementaryQuestion()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ComplementaryQuestion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          internet: true,
          development: true,
          configuration: true,
          cloud: true,
          internal: true,
          partner: true,
          users: 1,
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

    it('should delete a ComplementaryQuestion', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addComplementaryQuestionToCollectionIfMissing', () => {
      it('should add a ComplementaryQuestion to an empty array', () => {
        const complementaryQuestion: IComplementaryQuestion = { id: 123 };
        expectedResult = service.addComplementaryQuestionToCollectionIfMissing([], complementaryQuestion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(complementaryQuestion);
      });

      it('should not add a ComplementaryQuestion to an array that contains it', () => {
        const complementaryQuestion: IComplementaryQuestion = { id: 123 };
        const complementaryQuestionCollection: IComplementaryQuestion[] = [
          {
            ...complementaryQuestion,
          },
          { id: 456 },
        ];
        expectedResult = service.addComplementaryQuestionToCollectionIfMissing(complementaryQuestionCollection, complementaryQuestion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ComplementaryQuestion to an array that doesn't contain it", () => {
        const complementaryQuestion: IComplementaryQuestion = { id: 123 };
        const complementaryQuestionCollection: IComplementaryQuestion[] = [{ id: 456 }];
        expectedResult = service.addComplementaryQuestionToCollectionIfMissing(complementaryQuestionCollection, complementaryQuestion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(complementaryQuestion);
      });

      it('should add only unique ComplementaryQuestion to an array', () => {
        const complementaryQuestionArray: IComplementaryQuestion[] = [{ id: 123 }, { id: 456 }, { id: 64640 }];
        const complementaryQuestionCollection: IComplementaryQuestion[] = [{ id: 123 }];
        expectedResult = service.addComplementaryQuestionToCollectionIfMissing(
          complementaryQuestionCollection,
          ...complementaryQuestionArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const complementaryQuestion: IComplementaryQuestion = { id: 123 };
        const complementaryQuestion2: IComplementaryQuestion = { id: 456 };
        expectedResult = service.addComplementaryQuestionToCollectionIfMissing([], complementaryQuestion, complementaryQuestion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(complementaryQuestion);
        expect(expectedResult).toContain(complementaryQuestion2);
      });

      it('should accept null and undefined values', () => {
        const complementaryQuestion: IComplementaryQuestion = { id: 123 };
        expectedResult = service.addComplementaryQuestionToCollectionIfMissing([], null, complementaryQuestion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(complementaryQuestion);
      });

      it('should return initial array if no ComplementaryQuestion is added', () => {
        const complementaryQuestionCollection: IComplementaryQuestion[] = [{ id: 123 }];
        expectedResult = service.addComplementaryQuestionToCollectionIfMissing(complementaryQuestionCollection, undefined, null);
        expect(expectedResult).toEqual(complementaryQuestionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
