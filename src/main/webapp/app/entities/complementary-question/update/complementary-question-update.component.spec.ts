import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ComplementaryQuestionService } from '../service/complementary-question.service';
import { IComplementaryQuestion, ComplementaryQuestion } from '../complementary-question.model';
import { ISecurityInterview } from 'app/entities/security-interview/security-interview.model';
import { SecurityInterviewService } from 'app/entities/security-interview/service/security-interview.service';

import { ComplementaryQuestionUpdateComponent } from './complementary-question-update.component';

describe('ComplementaryQuestion Management Update Component', () => {
  let comp: ComplementaryQuestionUpdateComponent;
  let fixture: ComponentFixture<ComplementaryQuestionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let complementaryQuestionService: ComplementaryQuestionService;
  let securityInterviewService: SecurityInterviewService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ComplementaryQuestionUpdateComponent],
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
      .overrideTemplate(ComplementaryQuestionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ComplementaryQuestionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    complementaryQuestionService = TestBed.inject(ComplementaryQuestionService);
    securityInterviewService = TestBed.inject(SecurityInterviewService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SecurityInterview query and add missing value', () => {
      const complementaryQuestion: IComplementaryQuestion = { id: 456 };
      const securityInterview: ISecurityInterview = { id: 72843 };
      complementaryQuestion.securityInterview = securityInterview;

      const securityInterviewCollection: ISecurityInterview[] = [{ id: 82597 }];
      jest.spyOn(securityInterviewService, 'query').mockReturnValue(of(new HttpResponse({ body: securityInterviewCollection })));
      const additionalSecurityInterviews = [securityInterview];
      const expectedCollection: ISecurityInterview[] = [...additionalSecurityInterviews, ...securityInterviewCollection];
      jest.spyOn(securityInterviewService, 'addSecurityInterviewToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ complementaryQuestion });
      comp.ngOnInit();

      expect(securityInterviewService.query).toHaveBeenCalled();
      expect(securityInterviewService.addSecurityInterviewToCollectionIfMissing).toHaveBeenCalledWith(
        securityInterviewCollection,
        ...additionalSecurityInterviews
      );
      expect(comp.securityInterviewsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const complementaryQuestion: IComplementaryQuestion = { id: 456 };
      const securityInterview: ISecurityInterview = { id: 74466 };
      complementaryQuestion.securityInterview = securityInterview;

      activatedRoute.data = of({ complementaryQuestion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(complementaryQuestion));
      expect(comp.securityInterviewsSharedCollection).toContain(securityInterview);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ComplementaryQuestion>>();
      const complementaryQuestion = { id: 123 };
      jest.spyOn(complementaryQuestionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ complementaryQuestion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: complementaryQuestion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(complementaryQuestionService.update).toHaveBeenCalledWith(complementaryQuestion);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ComplementaryQuestion>>();
      const complementaryQuestion = new ComplementaryQuestion();
      jest.spyOn(complementaryQuestionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ complementaryQuestion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: complementaryQuestion }));
      saveSubject.complete();

      // THEN
      expect(complementaryQuestionService.create).toHaveBeenCalledWith(complementaryQuestion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ComplementaryQuestion>>();
      const complementaryQuestion = { id: 123 };
      jest.spyOn(complementaryQuestionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ complementaryQuestion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(complementaryQuestionService.update).toHaveBeenCalledWith(complementaryQuestion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSecurityInterviewById', () => {
      it('Should return tracked SecurityInterview primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSecurityInterviewById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
