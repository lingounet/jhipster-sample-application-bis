import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SensitiveDataService } from '../service/sensitive-data.service';
import { ISensitiveData, SensitiveData } from '../sensitive-data.model';
import { ISensitiveDataType } from 'app/entities/sensitive-data-type/sensitive-data-type.model';
import { SensitiveDataTypeService } from 'app/entities/sensitive-data-type/service/sensitive-data-type.service';
import { ISecurityInterview } from 'app/entities/security-interview/security-interview.model';
import { SecurityInterviewService } from 'app/entities/security-interview/service/security-interview.service';

import { SensitiveDataUpdateComponent } from './sensitive-data-update.component';

describe('SensitiveData Management Update Component', () => {
  let comp: SensitiveDataUpdateComponent;
  let fixture: ComponentFixture<SensitiveDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sensitiveDataService: SensitiveDataService;
  let sensitiveDataTypeService: SensitiveDataTypeService;
  let securityInterviewService: SecurityInterviewService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SensitiveDataUpdateComponent],
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
      .overrideTemplate(SensitiveDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SensitiveDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sensitiveDataService = TestBed.inject(SensitiveDataService);
    sensitiveDataTypeService = TestBed.inject(SensitiveDataTypeService);
    securityInterviewService = TestBed.inject(SecurityInterviewService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SensitiveDataType query and add missing value', () => {
      const sensitiveData: ISensitiveData = { id: 456 };
      const sensitiveDataType: ISensitiveDataType = { id: 56043 };
      sensitiveData.sensitiveDataType = sensitiveDataType;

      const sensitiveDataTypeCollection: ISensitiveDataType[] = [{ id: 29249 }];
      jest.spyOn(sensitiveDataTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: sensitiveDataTypeCollection })));
      const additionalSensitiveDataTypes = [sensitiveDataType];
      const expectedCollection: ISensitiveDataType[] = [...additionalSensitiveDataTypes, ...sensitiveDataTypeCollection];
      jest.spyOn(sensitiveDataTypeService, 'addSensitiveDataTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sensitiveData });
      comp.ngOnInit();

      expect(sensitiveDataTypeService.query).toHaveBeenCalled();
      expect(sensitiveDataTypeService.addSensitiveDataTypeToCollectionIfMissing).toHaveBeenCalledWith(
        sensitiveDataTypeCollection,
        ...additionalSensitiveDataTypes
      );
      expect(comp.sensitiveDataTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SecurityInterview query and add missing value', () => {
      const sensitiveData: ISensitiveData = { id: 456 };
      const securityInterview: ISecurityInterview = { id: 58211 };
      sensitiveData.securityInterview = securityInterview;

      const securityInterviewCollection: ISecurityInterview[] = [{ id: 25306 }];
      jest.spyOn(securityInterviewService, 'query').mockReturnValue(of(new HttpResponse({ body: securityInterviewCollection })));
      const additionalSecurityInterviews = [securityInterview];
      const expectedCollection: ISecurityInterview[] = [...additionalSecurityInterviews, ...securityInterviewCollection];
      jest.spyOn(securityInterviewService, 'addSecurityInterviewToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sensitiveData });
      comp.ngOnInit();

      expect(securityInterviewService.query).toHaveBeenCalled();
      expect(securityInterviewService.addSecurityInterviewToCollectionIfMissing).toHaveBeenCalledWith(
        securityInterviewCollection,
        ...additionalSecurityInterviews
      );
      expect(comp.securityInterviewsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sensitiveData: ISensitiveData = { id: 456 };
      const sensitiveDataType: ISensitiveDataType = { id: 83916 };
      sensitiveData.sensitiveDataType = sensitiveDataType;
      const securityInterview: ISecurityInterview = { id: 46671 };
      sensitiveData.securityInterview = securityInterview;

      activatedRoute.data = of({ sensitiveData });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sensitiveData));
      expect(comp.sensitiveDataTypesSharedCollection).toContain(sensitiveDataType);
      expect(comp.securityInterviewsSharedCollection).toContain(securityInterview);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SensitiveData>>();
      const sensitiveData = { id: 123 };
      jest.spyOn(sensitiveDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensitiveData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sensitiveData }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sensitiveDataService.update).toHaveBeenCalledWith(sensitiveData);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SensitiveData>>();
      const sensitiveData = new SensitiveData();
      jest.spyOn(sensitiveDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensitiveData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sensitiveData }));
      saveSubject.complete();

      // THEN
      expect(sensitiveDataService.create).toHaveBeenCalledWith(sensitiveData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SensitiveData>>();
      const sensitiveData = { id: 123 };
      jest.spyOn(sensitiveDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensitiveData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sensitiveDataService.update).toHaveBeenCalledWith(sensitiveData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSensitiveDataTypeById', () => {
      it('Should return tracked SensitiveDataType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSensitiveDataTypeById(0, entity);
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
