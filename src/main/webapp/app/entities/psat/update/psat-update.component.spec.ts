import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PsatService } from '../service/psat.service';
import { IPsat, Psat } from '../psat.model';
import { ISecurityInterview } from 'app/entities/security-interview/security-interview.model';
import { SecurityInterviewService } from 'app/entities/security-interview/service/security-interview.service';

import { PsatUpdateComponent } from './psat-update.component';

describe('Psat Management Update Component', () => {
  let comp: PsatUpdateComponent;
  let fixture: ComponentFixture<PsatUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let psatService: PsatService;
  let securityInterviewService: SecurityInterviewService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PsatUpdateComponent],
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
      .overrideTemplate(PsatUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PsatUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    psatService = TestBed.inject(PsatService);
    securityInterviewService = TestBed.inject(SecurityInterviewService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call securityInterview query and add missing value', () => {
      const psat: IPsat = { id: 456 };
      const securityInterview: ISecurityInterview = { id: 49657 };
      psat.securityInterview = securityInterview;

      const securityInterviewCollection: ISecurityInterview[] = [{ id: 40288 }];
      jest.spyOn(securityInterviewService, 'query').mockReturnValue(of(new HttpResponse({ body: securityInterviewCollection })));
      const expectedCollection: ISecurityInterview[] = [securityInterview, ...securityInterviewCollection];
      jest.spyOn(securityInterviewService, 'addSecurityInterviewToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ psat });
      comp.ngOnInit();

      expect(securityInterviewService.query).toHaveBeenCalled();
      expect(securityInterviewService.addSecurityInterviewToCollectionIfMissing).toHaveBeenCalledWith(
        securityInterviewCollection,
        securityInterview
      );
      expect(comp.securityInterviewsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const psat: IPsat = { id: 456 };
      const securityInterview: ISecurityInterview = { id: 63029 };
      psat.securityInterview = securityInterview;

      activatedRoute.data = of({ psat });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(psat));
      expect(comp.securityInterviewsCollection).toContain(securityInterview);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Psat>>();
      const psat = { id: 123 };
      jest.spyOn(psatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ psat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: psat }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(psatService.update).toHaveBeenCalledWith(psat);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Psat>>();
      const psat = new Psat();
      jest.spyOn(psatService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ psat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: psat }));
      saveSubject.complete();

      // THEN
      expect(psatService.create).toHaveBeenCalledWith(psat);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Psat>>();
      const psat = { id: 123 };
      jest.spyOn(psatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ psat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(psatService.update).toHaveBeenCalledWith(psat);
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
