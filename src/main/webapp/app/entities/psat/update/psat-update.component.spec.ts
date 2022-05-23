import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PsatService } from '../service/psat.service';
import { IPsat, Psat } from '../psat.model';

import { PsatUpdateComponent } from './psat-update.component';

describe('Psat Management Update Component', () => {
  let comp: PsatUpdateComponent;
  let fixture: ComponentFixture<PsatUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let psatService: PsatService;

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

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const psat: IPsat = { id: 456 };

      activatedRoute.data = of({ psat });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(psat));
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
});
