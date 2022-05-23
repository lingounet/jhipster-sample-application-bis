import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ISensitiveData, SensitiveData } from '../sensitive-data.model';
import { SensitiveDataService } from '../service/sensitive-data.service';

import { SensitiveDataRoutingResolveService } from './sensitive-data-routing-resolve.service';

describe('SensitiveData routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SensitiveDataRoutingResolveService;
  let service: SensitiveDataService;
  let resultSensitiveData: ISensitiveData | undefined;

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
    routingResolveService = TestBed.inject(SensitiveDataRoutingResolveService);
    service = TestBed.inject(SensitiveDataService);
    resultSensitiveData = undefined;
  });

  describe('resolve', () => {
    it('should return ISensitiveData returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSensitiveData = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSensitiveData).toEqual({ id: 123 });
    });

    it('should return new ISensitiveData if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSensitiveData = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSensitiveData).toEqual(new SensitiveData());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SensitiveData })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSensitiveData = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSensitiveData).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
