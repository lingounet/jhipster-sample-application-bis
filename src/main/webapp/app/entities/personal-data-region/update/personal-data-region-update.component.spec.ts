import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PersonalDataRegionService } from '../service/personal-data-region.service';
import { IPersonalDataRegion, PersonalDataRegion } from '../personal-data-region.model';

import { PersonalDataRegionUpdateComponent } from './personal-data-region-update.component';

describe('PersonalDataRegion Management Update Component', () => {
  let comp: PersonalDataRegionUpdateComponent;
  let fixture: ComponentFixture<PersonalDataRegionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let personalDataRegionService: PersonalDataRegionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PersonalDataRegionUpdateComponent],
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
      .overrideTemplate(PersonalDataRegionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PersonalDataRegionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    personalDataRegionService = TestBed.inject(PersonalDataRegionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const personalDataRegion: IPersonalDataRegion = { id: 456 };

      activatedRoute.data = of({ personalDataRegion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(personalDataRegion));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PersonalDataRegion>>();
      const personalDataRegion = { id: 123 };
      jest.spyOn(personalDataRegionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personalDataRegion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personalDataRegion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(personalDataRegionService.update).toHaveBeenCalledWith(personalDataRegion);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PersonalDataRegion>>();
      const personalDataRegion = new PersonalDataRegion();
      jest.spyOn(personalDataRegionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personalDataRegion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personalDataRegion }));
      saveSubject.complete();

      // THEN
      expect(personalDataRegionService.create).toHaveBeenCalledWith(personalDataRegion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PersonalDataRegion>>();
      const personalDataRegion = { id: 123 };
      jest.spyOn(personalDataRegionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personalDataRegion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(personalDataRegionService.update).toHaveBeenCalledWith(personalDataRegion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
