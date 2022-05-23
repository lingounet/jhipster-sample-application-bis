import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SecurityInterviewService } from '../service/security-interview.service';
import { ISecurityInterview, SecurityInterview } from '../security-interview.model';
import { IPsat } from 'app/entities/psat/psat.model';
import { PsatService } from 'app/entities/psat/service/psat.service';
import { IAvailability } from 'app/entities/availability/availability.model';
import { AvailabilityService } from 'app/entities/availability/service/availability.service';

import { SecurityInterviewUpdateComponent } from './security-interview-update.component';

describe('SecurityInterview Management Update Component', () => {
  let comp: SecurityInterviewUpdateComponent;
  let fixture: ComponentFixture<SecurityInterviewUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let securityInterviewService: SecurityInterviewService;
  let psatService: PsatService;
  let availabilityService: AvailabilityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SecurityInterviewUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SecurityInterviewUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SecurityInterviewUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    securityInterviewService = TestBed.inject(SecurityInterviewService);
    psatService = TestBed.inject(PsatService);
    availabilityService = TestBed.inject(AvailabilityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call psat query and add missing value', () => {
      const securityInterview: ISecurityInterview = { id: 456 };
      const psat: IPsat = { id: 96453 };
      securityInterview.psat = psat;

      const psatCollection: IPsat[] = [{ id: 8860 }];
      jest.spyOn(psatService, 'query').mockReturnValue(of(new HttpResponse({ body: psatCollection })));
      const expectedCollection: IPsat[] = [psat, ...psatCollection];
      jest.spyOn(psatService, 'addPsatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ securityInterview });
      comp.ngOnInit();

      expect(psatService.query).toHaveBeenCalled();
      expect(psatService.addPsatToCollectionIfMissing).toHaveBeenCalledWith(psatCollection, psat);
      expect(comp.psatsCollection).toEqual(expectedCollection);
    });

    it('Should call availability query and add missing value', () => {
      const securityInterview: ISecurityInterview = { id: 456 };
      const availability: IAvailability = { id: 81054 };
      securityInterview.availability = availability;

      const availabilityCollection: IAvailability[] = [{ id: 45460 }];
      jest.spyOn(availabilityService, 'query').mockReturnValue(of(new HttpResponse({ body: availabilityCollection })));
      const expectedCollection: IAvailability[] = [availability, ...availabilityCollection];
      jest.spyOn(availabilityService, 'addAvailabilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ securityInterview });
      comp.ngOnInit();

      expect(availabilityService.query).toHaveBeenCalled();
      expect(availabilityService.addAvailabilityToCollectionIfMissing).toHaveBeenCalledWith(availabilityCollection, availability);
      expect(comp.availabilitiesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const securityInterview: ISecurityInterview = { id: 456 };
      const psat: IPsat = { id: 99438 };
      securityInterview.psat = psat;
      const availability: IAvailability = { id: 12096 };
      securityInterview.availability = availability;

      activatedRoute.data = of({ securityInterview });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(securityInterview));
      expect(comp.psatsCollection).toContain(psat);
      expect(comp.availabilitiesCollection).toContain(availability);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SecurityInterview>>();
      const securityInterview = { id: 123 };
      jest.spyOn(securityInterviewService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ securityInterview });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: securityInterview }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(securityInterviewService.update).toHaveBeenCalledWith(securityInterview);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SecurityInterview>>();
      const securityInterview = new SecurityInterview();
      jest.spyOn(securityInterviewService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ securityInterview });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: securityInterview }));
      saveSubject.complete();

      // THEN
      expect(securityInterviewService.create).toHaveBeenCalledWith(securityInterview);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SecurityInterview>>();
      const securityInterview = { id: 123 };
      jest.spyOn(securityInterviewService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ securityInterview });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(securityInterviewService.update).toHaveBeenCalledWith(securityInterview);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPsatById', () => {
      it('Should return tracked Psat primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPsatById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAvailabilityById', () => {
      it('Should return tracked Availability primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAvailabilityById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
