import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ISensitiveDataType, SensitiveDataType } from '../sensitive-data-type.model';
import { SensitiveDataTypeService } from '../service/sensitive-data-type.service';

import { SensitiveDataTypeRoutingResolveService } from './sensitive-data-type-routing-resolve.service';

describe('SensitiveDataType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SensitiveDataTypeRoutingResolveService;
  let service: SensitiveDataTypeService;
  let resultSensitiveDataType: ISensitiveDataType | undefined;

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
    routingResolveService = TestBed.inject(SensitiveDataTypeRoutingResolveService);
    service = TestBed.inject(SensitiveDataTypeService);
    resultSensitiveDataType = undefined;
  });

  describe('resolve', () => {
    it('should return ISensitiveDataType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSensitiveDataType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSensitiveDataType).toEqual({ id: 123 });
    });

    it('should return new ISensitiveDataType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSensitiveDataType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSensitiveDataType).toEqual(new SensitiveDataType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SensitiveDataType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSensitiveDataType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSensitiveDataType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
