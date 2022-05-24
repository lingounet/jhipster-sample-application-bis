import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IcrfAssessmentService } from '../service/icrf-assessment.service';
import { IIcrfAssessment, IcrfAssessment } from '../icrf-assessment.model';

import { IcrfAssessmentUpdateComponent } from './icrf-assessment-update.component';

describe('IcrfAssessment Management Update Component', () => {
  let comp: IcrfAssessmentUpdateComponent;
  let fixture: ComponentFixture<IcrfAssessmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let icrfAssessmentService: IcrfAssessmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IcrfAssessmentUpdateComponent],
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
      .overrideTemplate(IcrfAssessmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IcrfAssessmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    icrfAssessmentService = TestBed.inject(IcrfAssessmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const icrfAssessment: IIcrfAssessment = { id: 456 };

      activatedRoute.data = of({ icrfAssessment });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(icrfAssessment));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IcrfAssessment>>();
      const icrfAssessment = { id: 123 };
      jest.spyOn(icrfAssessmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ icrfAssessment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: icrfAssessment }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(icrfAssessmentService.update).toHaveBeenCalledWith(icrfAssessment);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IcrfAssessment>>();
      const icrfAssessment = new IcrfAssessment();
      jest.spyOn(icrfAssessmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ icrfAssessment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: icrfAssessment }));
      saveSubject.complete();

      // THEN
      expect(icrfAssessmentService.create).toHaveBeenCalledWith(icrfAssessment);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IcrfAssessment>>();
      const icrfAssessment = { id: 123 };
      jest.spyOn(icrfAssessmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ icrfAssessment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(icrfAssessmentService.update).toHaveBeenCalledWith(icrfAssessment);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
