import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPersonalDataRegion, PersonalDataRegion } from '../personal-data-region.model';
import { PersonalDataRegionService } from '../service/personal-data-region.service';

import { PersonalDataRegionRoutingResolveService } from './personal-data-region-routing-resolve.service';

describe('PersonalDataRegion routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PersonalDataRegionRoutingResolveService;
  let service: PersonalDataRegionService;
  let resultPersonalDataRegion: IPersonalDataRegion | undefined;

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
    routingResolveService = TestBed.inject(PersonalDataRegionRoutingResolveService);
    service = TestBed.inject(PersonalDataRegionService);
    resultPersonalDataRegion = undefined;
  });

  describe('resolve', () => {
    it('should return IPersonalDataRegion returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPersonalDataRegion = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPersonalDataRegion).toEqual({ id: 123 });
    });

    it('should return new IPersonalDataRegion if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPersonalDataRegion = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPersonalDataRegion).toEqual(new PersonalDataRegion());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PersonalDataRegion })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPersonalDataRegion = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPersonalDataRegion).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
