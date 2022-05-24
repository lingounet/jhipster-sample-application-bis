import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IcrfService } from '../service/icrf.service';
import { IIcrf, Icrf } from '../icrf.model';
import { IIcrfAssessment } from 'app/entities/icrf-assessment/icrf-assessment.model';
import { IcrfAssessmentService } from 'app/entities/icrf-assessment/service/icrf-assessment.service';
import { IIdentity } from 'app/entities/identity/identity.model';
import { IdentityService } from 'app/entities/identity/service/identity.service';

import { IcrfUpdateComponent } from './icrf-update.component';

describe('Icrf Management Update Component', () => {
  let comp: IcrfUpdateComponent;
  let fixture: ComponentFixture<IcrfUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let icrfService: IcrfService;
  let icrfAssessmentService: IcrfAssessmentService;
  let identityService: IdentityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IcrfUpdateComponent],
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
      .overrideTemplate(IcrfUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IcrfUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    icrfService = TestBed.inject(IcrfService);
    icrfAssessmentService = TestBed.inject(IcrfAssessmentService);
    identityService = TestBed.inject(IdentityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call IcrfAssessment query and add missing value', () => {
      const icrf: IIcrf = { id: 456 };
      const icrfAssessment: IIcrfAssessment = { id: 87463 };
      icrf.icrfAssessment = icrfAssessment;

      const icrfAssessmentCollection: IIcrfAssessment[] = [{ id: 70354 }];
      jest.spyOn(icrfAssessmentService, 'query').mockReturnValue(of(new HttpResponse({ body: icrfAssessmentCollection })));
      const additionalIcrfAssessments = [icrfAssessment];
      const expectedCollection: IIcrfAssessment[] = [...additionalIcrfAssessments, ...icrfAssessmentCollection];
      jest.spyOn(icrfAssessmentService, 'addIcrfAssessmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ icrf });
      comp.ngOnInit();

      expect(icrfAssessmentService.query).toHaveBeenCalled();
      expect(icrfAssessmentService.addIcrfAssessmentToCollectionIfMissing).toHaveBeenCalledWith(
        icrfAssessmentCollection,
        ...additionalIcrfAssessments
      );
      expect(comp.icrfAssessmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Identity query and add missing value', () => {
      const icrf: IIcrf = { id: 456 };
      const identity: IIdentity = { id: 91365 };
      icrf.identity = identity;

      const identityCollection: IIdentity[] = [{ id: 53002 }];
      jest.spyOn(identityService, 'query').mockReturnValue(of(new HttpResponse({ body: identityCollection })));
      const additionalIdentities = [identity];
      const expectedCollection: IIdentity[] = [...additionalIdentities, ...identityCollection];
      jest.spyOn(identityService, 'addIdentityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ icrf });
      comp.ngOnInit();

      expect(identityService.query).toHaveBeenCalled();
      expect(identityService.addIdentityToCollectionIfMissing).toHaveBeenCalledWith(identityCollection, ...additionalIdentities);
      expect(comp.identitiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const icrf: IIcrf = { id: 456 };
      const icrfAssessment: IIcrfAssessment = { id: 76709 };
      icrf.icrfAssessment = icrfAssessment;
      const identity: IIdentity = { id: 4056 };
      icrf.identity = identity;

      activatedRoute.data = of({ icrf });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(icrf));
      expect(comp.icrfAssessmentsSharedCollection).toContain(icrfAssessment);
      expect(comp.identitiesSharedCollection).toContain(identity);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Icrf>>();
      const icrf = { id: 123 };
      jest.spyOn(icrfService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ icrf });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: icrf }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(icrfService.update).toHaveBeenCalledWith(icrf);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Icrf>>();
      const icrf = new Icrf();
      jest.spyOn(icrfService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ icrf });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: icrf }));
      saveSubject.complete();

      // THEN
      expect(icrfService.create).toHaveBeenCalledWith(icrf);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Icrf>>();
      const icrf = { id: 123 };
      jest.spyOn(icrfService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ icrf });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(icrfService.update).toHaveBeenCalledWith(icrf);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackIcrfAssessmentById', () => {
      it('Should return tracked IcrfAssessment primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackIcrfAssessmentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackIdentityById', () => {
      it('Should return tracked Identity primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackIdentityById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
