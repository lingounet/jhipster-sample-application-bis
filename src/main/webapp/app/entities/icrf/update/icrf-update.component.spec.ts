import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IcrfService } from '../service/icrf.service';
import { IIcrf, Icrf } from '../icrf.model';
import { IIcrfStatus } from 'app/entities/icrf-status/icrf-status.model';
import { IcrfStatusService } from 'app/entities/icrf-status/service/icrf-status.service';
import { ISecurityInterview } from 'app/entities/security-interview/security-interview.model';
import { SecurityInterviewService } from 'app/entities/security-interview/service/security-interview.service';

import { IcrfUpdateComponent } from './icrf-update.component';

describe('Icrf Management Update Component', () => {
  let comp: IcrfUpdateComponent;
  let fixture: ComponentFixture<IcrfUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let icrfService: IcrfService;
  let icrfStatusService: IcrfStatusService;
  let securityInterviewService: SecurityInterviewService;

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
    icrfStatusService = TestBed.inject(IcrfStatusService);
    securityInterviewService = TestBed.inject(SecurityInterviewService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call icrfStatus query and add missing value', () => {
      const icrf: IIcrf = { id: 456 };
      const icrfStatus: IIcrfStatus = { id: 54720 };
      icrf.icrfStatus = icrfStatus;

      const icrfStatusCollection: IIcrfStatus[] = [{ id: 45446 }];
      jest.spyOn(icrfStatusService, 'query').mockReturnValue(of(new HttpResponse({ body: icrfStatusCollection })));
      const expectedCollection: IIcrfStatus[] = [icrfStatus, ...icrfStatusCollection];
      jest.spyOn(icrfStatusService, 'addIcrfStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ icrf });
      comp.ngOnInit();

      expect(icrfStatusService.query).toHaveBeenCalled();
      expect(icrfStatusService.addIcrfStatusToCollectionIfMissing).toHaveBeenCalledWith(icrfStatusCollection, icrfStatus);
      expect(comp.icrfStatusesCollection).toEqual(expectedCollection);
    });

    it('Should call SecurityInterview query and add missing value', () => {
      const icrf: IIcrf = { id: 456 };
      const securityInterview: ISecurityInterview = { id: 32914 };
      icrf.securityInterview = securityInterview;

      const securityInterviewCollection: ISecurityInterview[] = [{ id: 45666 }];
      jest.spyOn(securityInterviewService, 'query').mockReturnValue(of(new HttpResponse({ body: securityInterviewCollection })));
      const additionalSecurityInterviews = [securityInterview];
      const expectedCollection: ISecurityInterview[] = [...additionalSecurityInterviews, ...securityInterviewCollection];
      jest.spyOn(securityInterviewService, 'addSecurityInterviewToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ icrf });
      comp.ngOnInit();

      expect(securityInterviewService.query).toHaveBeenCalled();
      expect(securityInterviewService.addSecurityInterviewToCollectionIfMissing).toHaveBeenCalledWith(
        securityInterviewCollection,
        ...additionalSecurityInterviews
      );
      expect(comp.securityInterviewsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const icrf: IIcrf = { id: 456 };
      const icrfStatus: IIcrfStatus = { id: 85773 };
      icrf.icrfStatus = icrfStatus;
      const securityInterview: ISecurityInterview = { id: 22401 };
      icrf.securityInterview = securityInterview;

      activatedRoute.data = of({ icrf });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(icrf));
      expect(comp.icrfStatusesCollection).toContain(icrfStatus);
      expect(comp.securityInterviewsSharedCollection).toContain(securityInterview);
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
    describe('trackIcrfStatusById', () => {
      it('Should return tracked IcrfStatus primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackIcrfStatusById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSecurityInterviewById', () => {
      it('Should return tracked SecurityInterview primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSecurityInterviewById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
