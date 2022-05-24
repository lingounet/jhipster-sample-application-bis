import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IComplementaryQuestion, ComplementaryQuestion } from '../complementary-question.model';
import { ComplementaryQuestionService } from '../service/complementary-question.service';

import { ComplementaryQuestionRoutingResolveService } from './complementary-question-routing-resolve.service';

describe('ComplementaryQuestion routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ComplementaryQuestionRoutingResolveService;
  let service: ComplementaryQuestionService;
  let resultComplementaryQuestion: IComplementaryQuestion | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(ComplementaryQuestionRoutingResolveService);
    service = TestBed.inject(ComplementaryQuestionService);
    resultComplementaryQuestion = undefined;
  });

  describe('resolve', () => {
    it('should return IComplementaryQuestion returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultComplementaryQuestion = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultComplementaryQuestion).toEqual({ id: 123 });
    });

    it('should return new IComplementaryQuestion if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultComplementaryQuestion = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultComplementaryQuestion).toEqual(new ComplementaryQuestion());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ComplementaryQuestion })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultComplementaryQuestion = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultComplementaryQuestion).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
