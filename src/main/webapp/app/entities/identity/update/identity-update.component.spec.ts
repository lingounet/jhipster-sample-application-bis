import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IdentityService } from '../service/identity.service';
import { IIdentity, Identity } from '../identity.model';
import { IPsat } from 'app/entities/psat/psat.model';
import { PsatService } from 'app/entities/psat/service/psat.service';
import { IAvailability } from 'app/entities/availability/availability.model';
import { AvailabilityService } from 'app/entities/availability/service/availability.service';
import { IApplicationType } from 'app/entities/application-type/application-type.model';
import { ApplicationTypeService } from 'app/entities/application-type/service/application-type.service';

import { IdentityUpdateComponent } from './identity-update.component';

describe('Identity Management Update Component', () => {
  let comp: IdentityUpdateComponent;
  let fixture: ComponentFixture<IdentityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let identityService: IdentityService;
  let psatService: PsatService;
  let availabilityService: AvailabilityService;
  let applicationTypeService: ApplicationTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IdentityUpdateComponent],
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
      .overrideTemplate(IdentityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IdentityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    identityService = TestBed.inject(IdentityService);
    psatService = TestBed.inject(PsatService);
    availabilityService = TestBed.inject(AvailabilityService);
    applicationTypeService = TestBed.inject(ApplicationTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call psat query and add missing value', () => {
      const identity: IIdentity = { id: 456 };
      const psat: IPsat = { id: 28678 };
      identity.psat = psat;

      const psatCollection: IPsat[] = [{ id: 54642 }];
      jest.spyOn(psatService, 'query').mockReturnValue(of(new HttpResponse({ body: psatCollection })));
      const expectedCollection: IPsat[] = [psat, ...psatCollection];
      jest.spyOn(psatService, 'addPsatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ identity });
      comp.ngOnInit();

      expect(psatService.query).toHaveBeenCalled();
      expect(psatService.addPsatToCollectionIfMissing).toHaveBeenCalledWith(psatCollection, psat);
      expect(comp.psatsCollection).toEqual(expectedCollection);
    });

    it('Should call availability query and add missing value', () => {
      const identity: IIdentity = { id: 456 };
      const availability: IAvailability = { id: 80164 };
      identity.availability = availability;

      const availabilityCollection: IAvailability[] = [{ id: 99009 }];
      jest.spyOn(availabilityService, 'query').mockReturnValue(of(new HttpResponse({ body: availabilityCollection })));
      const expectedCollection: IAvailability[] = [availability, ...availabilityCollection];
      jest.spyOn(availabilityService, 'addAvailabilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ identity });
      comp.ngOnInit();

      expect(availabilityService.query).toHaveBeenCalled();
      expect(availabilityService.addAvailabilityToCollectionIfMissing).toHaveBeenCalledWith(availabilityCollection, availability);
      expect(comp.availabilitiesCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationType query and add missing value', () => {
      const identity: IIdentity = { id: 456 };
      const applicationType: IApplicationType = { id: 14316 };
      identity.applicationType = applicationType;

      const applicationTypeCollection: IApplicationType[] = [{ id: 29571 }];
      jest.spyOn(applicationTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationTypeCollection })));
      const additionalApplicationTypes = [applicationType];
      const expectedCollection: IApplicationType[] = [...additionalApplicationTypes, ...applicationTypeCollection];
      jest.spyOn(applicationTypeService, 'addApplicationTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ identity });
      comp.ngOnInit();

      expect(applicationTypeService.query).toHaveBeenCalled();
      expect(applicationTypeService.addApplicationTypeToCollectionIfMissing).toHaveBeenCalledWith(
        applicationTypeCollection,
        ...additionalApplicationTypes
      );
      expect(comp.applicationTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const identity: IIdentity = { id: 456 };
      const psat: IPsat = { id: 15870 };
      identity.psat = psat;
      const availability: IAvailability = { id: 66902 };
      identity.availability = availability;
      const applicationType: IApplicationType = { id: 37988 };
      identity.applicationType = applicationType;

      activatedRoute.data = of({ identity });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(identity));
      expect(comp.psatsCollection).toContain(psat);
      expect(comp.availabilitiesCollection).toContain(availability);
      expect(comp.applicationTypesSharedCollection).toContain(applicationType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Identity>>();
      const identity = { id: 123 };
      jest.spyOn(identityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ identity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: identity }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(identityService.update).toHaveBeenCalledWith(identity);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Identity>>();
      const identity = new Identity();
      jest.spyOn(identityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ identity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: identity }));
      saveSubject.complete();

      // THEN
      expect(identityService.create).toHaveBeenCalledWith(identity);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Identity>>();
      const identity = { id: 123 };
      jest.spyOn(identityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ identity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(identityService.update).toHaveBeenCalledWith(identity);
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

    describe('trackApplicationTypeById', () => {
      it('Should return tracked ApplicationType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApplicationTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
