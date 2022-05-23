import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PersonalDataTypeService } from '../service/personal-data-type.service';
import { IPersonalDataType, PersonalDataType } from '../personal-data-type.model';
import { IPersonalDataRegion } from 'app/entities/personal-data-region/personal-data-region.model';
import { PersonalDataRegionService } from 'app/entities/personal-data-region/service/personal-data-region.service';

import { PersonalDataTypeUpdateComponent } from './personal-data-type-update.component';

describe('PersonalDataType Management Update Component', () => {
  let comp: PersonalDataTypeUpdateComponent;
  let fixture: ComponentFixture<PersonalDataTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let personalDataTypeService: PersonalDataTypeService;
  let personalDataRegionService: PersonalDataRegionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PersonalDataTypeUpdateComponent],
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
      .overrideTemplate(PersonalDataTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PersonalDataTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    personalDataTypeService = TestBed.inject(PersonalDataTypeService);
    personalDataRegionService = TestBed.inject(PersonalDataRegionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PersonalDataRegion query and add missing value', () => {
      const personalDataType: IPersonalDataType = { id: 456 };
      const personalDataRegion: IPersonalDataRegion = { id: 21938 };
      personalDataType.personalDataRegion = personalDataRegion;

      const personalDataRegionCollection: IPersonalDataRegion[] = [{ id: 59910 }];
      jest.spyOn(personalDataRegionService, 'query').mockReturnValue(of(new HttpResponse({ body: personalDataRegionCollection })));
      const additionalPersonalDataRegions = [personalDataRegion];
      const expectedCollection: IPersonalDataRegion[] = [...additionalPersonalDataRegions, ...personalDataRegionCollection];
      jest.spyOn(personalDataRegionService, 'addPersonalDataRegionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ personalDataType });
      comp.ngOnInit();

      expect(personalDataRegionService.query).toHaveBeenCalled();
      expect(personalDataRegionService.addPersonalDataRegionToCollectionIfMissing).toHaveBeenCalledWith(
        personalDataRegionCollection,
        ...additionalPersonalDataRegions
      );
      expect(comp.personalDataRegionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const personalDataType: IPersonalDataType = { id: 456 };
      const personalDataRegion: IPersonalDataRegion = { id: 76334 };
      personalDataType.personalDataRegion = personalDataRegion;

      activatedRoute.data = of({ personalDataType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(personalDataType));
      expect(comp.personalDataRegionsSharedCollection).toContain(personalDataRegion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PersonalDataType>>();
      const personalDataType = { id: 123 };
      jest.spyOn(personalDataTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personalDataType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personalDataType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(personalDataTypeService.update).toHaveBeenCalledWith(personalDataType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PersonalDataType>>();
      const personalDataType = new PersonalDataType();
      jest.spyOn(personalDataTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personalDataType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personalDataType }));
      saveSubject.complete();

      // THEN
      expect(personalDataTypeService.create).toHaveBeenCalledWith(personalDataType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PersonalDataType>>();
      const personalDataType = { id: 123 };
      jest.spyOn(personalDataTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personalDataType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(personalDataTypeService.update).toHaveBeenCalledWith(personalDataType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPersonalDataRegionById', () => {
      it('Should return tracked PersonalDataRegion primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPersonalDataRegionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
