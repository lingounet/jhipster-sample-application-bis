import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ComplementaryQuestionService } from '../service/complementary-question.service';
import { IComplementaryQuestion, ComplementaryQuestion } from '../complementary-question.model';
import { IIdentity } from 'app/entities/identity/identity.model';
import { IdentityService } from 'app/entities/identity/service/identity.service';

import { ComplementaryQuestionUpdateComponent } from './complementary-question-update.component';

describe('ComplementaryQuestion Management Update Component', () => {
  let comp: ComplementaryQuestionUpdateComponent;
  let fixture: ComponentFixture<ComplementaryQuestionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let complementaryQuestionService: ComplementaryQuestionService;
  let identityService: IdentityService;

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
    identityService = TestBed.inject(IdentityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Identity query and add missing value', () => {
      const complementaryQuestion: IComplementaryQuestion = { id: 456 };
      const identity: IIdentity = { id: 86326 };
      complementaryQuestion.identity = identity;

      const identityCollection: IIdentity[] = [{ id: 42333 }];
      jest.spyOn(identityService, 'query').mockReturnValue(of(new HttpResponse({ body: identityCollection })));
      const additionalIdentities = [identity];
      const expectedCollection: IIdentity[] = [...additionalIdentities, ...identityCollection];
      jest.spyOn(identityService, 'addIdentityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ complementaryQuestion });
      comp.ngOnInit();

      expect(identityService.query).toHaveBeenCalled();
      expect(identityService.addIdentityToCollectionIfMissing).toHaveBeenCalledWith(identityCollection, ...additionalIdentities);
      expect(comp.identitiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const complementaryQuestion: IComplementaryQuestion = { id: 456 };
      const identity: IIdentity = { id: 76324 };
      complementaryQuestion.identity = identity;

      activatedRoute.data = of({ complementaryQuestion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(complementaryQuestion));
      expect(comp.identitiesSharedCollection).toContain(identity);
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
    describe('trackIdentityById', () => {
      it('Should return tracked Identity primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackIdentityById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
