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
import { IIdentity } from 'app/entities/identity/identity.model';
import { IdentityService } from 'app/entities/identity/service/identity.service';

import { PersonalDataUpdateComponent } from './personal-data-update.component';

describe('PersonalData Management Update Component', () => {
  let comp: PersonalDataUpdateComponent;
  let fixture: ComponentFixture<PersonalDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let personalDataService: PersonalDataService;
  let personalDataTypeService: PersonalDataTypeService;
  let identityService: IdentityService;

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
    identityService = TestBed.inject(IdentityService);

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

    it('Should call Identity query and add missing value', () => {
      const personalData: IPersonalData = { id: 456 };
      const identity: IIdentity = { id: 3043 };
      personalData.identity = identity;

      const identityCollection: IIdentity[] = [{ id: 63485 }];
      jest.spyOn(identityService, 'query').mockReturnValue(of(new HttpResponse({ body: identityCollection })));
      const additionalIdentities = [identity];
      const expectedCollection: IIdentity[] = [...additionalIdentities, ...identityCollection];
      jest.spyOn(identityService, 'addIdentityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ personalData });
      comp.ngOnInit();

      expect(identityService.query).toHaveBeenCalled();
      expect(identityService.addIdentityToCollectionIfMissing).toHaveBeenCalledWith(identityCollection, ...additionalIdentities);
      expect(comp.identitiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const personalData: IPersonalData = { id: 456 };
      const personalDataType: IPersonalDataType = { id: 22827 };
      personalData.personalDataType = personalDataType;
      const identity: IIdentity = { id: 19251 };
      personalData.identity = identity;

      activatedRoute.data = of({ personalData });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(personalData));
      expect(comp.personalDataTypesSharedCollection).toContain(personalDataType);
      expect(comp.identitiesSharedCollection).toContain(identity);
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

    describe('trackIdentityById', () => {
      it('Should return tracked Identity primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackIdentityById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
