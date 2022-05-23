import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PersonalDataService } from '../service/personal-data.service';
import { IPersonalData, PersonalData } from '../personal-data.model';
import { IPersonalDataType } from 'app/entities/personal-data-type/personal-data-type.model';
import { PersonalDataTypeService } from 'app/entities/personal-data-type/service/personal-data-type.service';
import { ISecurityInterview } from 'app/entities/security-interview/security-interview.model';
import { SecurityInterviewService } from 'app/entities/security-interview/service/security-interview.service';

import { PersonalDataUpdateComponent } from './personal-data-update.component';

describe('PersonalData Management Update Component', () => {
  let comp: PersonalDataUpdateComponent;
  let fixture: ComponentFixture<PersonalDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let personalDataService: PersonalDataService;
  let personalDataTypeService: PersonalDataTypeService;
  let securityInterviewService: SecurityInterviewService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PersonalDataUpdateComponent],
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
      .overrideTemplate(PersonalDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PersonalDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    personalDataService = TestBed.inject(PersonalDataService);
    personalDataTypeService = TestBed.inject(PersonalDataTypeService);
    securityInterviewService = TestBed.inject(SecurityInterviewService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PersonalDataType query and add missing value', () => {
      const personalData: IPersonalData = { id: 456 };
      const personalDataType: IPersonalDataType = { id: 81549 };
      personalData.personalDataType = personalDataType;

      const personalDataTypeCollection: IPersonalDataType[] = [{ id: 24983 }];
      jest.spyOn(personalDataTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: personalDataTypeCollection })));
      const additionalPersonalDataTypes = [personalDataType];
      const expectedCollection: IPersonalDataType[] = [...additionalPersonalDataTypes, ...personalDataTypeCollection];
      jest.spyOn(personalDataTypeService, 'addPersonalDataTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ personalData });
      comp.ngOnInit();

      expect(personalDataTypeService.query).toHaveBeenCalled();
      expect(personalDataTypeService.addPersonalDataTypeToCollectionIfMissing).toHaveBeenCalledWith(
        personalDataTypeCollection,
        ...additionalPersonalDataTypes
      );
      expect(comp.personalDataTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SecurityInterview query and add missing value', () => {
      const personalData: IPersonalData = { id: 456 };
      const securityInterview: ISecurityInterview = { id: 8576 };
      personalData.securityInterview = securityInterview;

      const securityInterviewCollection: ISecurityInterview[] = [{ id: 11784 }];
      jest.spyOn(securityInterviewService, 'query').mockReturnValue(of(new HttpResponse({ body: securityInterviewCollection })));
      const additionalSecurityInterviews = [securityInterview];
      const expectedCollection: ISecurityInterview[] = [...additionalSecurityInterviews, ...securityInterviewCollection];
      jest.spyOn(securityInterviewService, 'addSecurityInterviewToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ personalData });
      comp.ngOnInit();

      expect(securityInterviewService.query).toHaveBeenCalled();
      expect(securityInterviewService.addSecurityInterviewToCollectionIfMissing).toHaveBeenCalledWith(
        securityInterviewCollection,
        ...additionalSecurityInterviews
      );
      expect(comp.securityInterviewsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const personalData: IPersonalData = { id: 456 };
      const personalDataType: IPersonalDataType = { id: 22827 };
      personalData.personalDataType = personalDataType;
      const securityInterview: ISecurityInterview = { id: 48289 };
      personalData.securityInterview = securityInterview;

      activatedRoute.data = of({ personalData });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(personalData));
      expect(comp.personalDataTypesSharedCollection).toContain(personalDataType);
      expect(comp.securityInterviewsSharedCollection).toContain(securityInterview);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PersonalData>>();
      const personalData = { id: 123 };
      jest.spyOn(personalDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personalData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personalData }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(personalDataService.update).toHaveBeenCalledWith(personalData);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PersonalData>>();
      const personalData = new PersonalData();
      jest.spyOn(personalDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personalData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personalData }));
      saveSubject.complete();

      // THEN
      expect(personalDataService.create).toHaveBeenCalledWith(personalData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PersonalData>>();
      const personalData = { id: 123 };
      jest.spyOn(personalDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personalData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(personalDataService.update).toHaveBeenCalledWith(personalData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPersonalDataTypeById', () => {
      it('Should return tracked PersonalDataType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPersonalDataTypeById(0, entity);
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
