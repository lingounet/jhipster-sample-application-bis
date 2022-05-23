import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AmlService } from '../service/aml.service';
import { IAml, Aml } from '../aml.model';

import { AmlUpdateComponent } from './aml-update.component';

describe('Aml Management Update Component', () => {
  let comp: AmlUpdateComponent;
  let fixture: ComponentFixture<AmlUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let amlService: AmlService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AmlUpdateComponent],
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
      .overrideTemplate(AmlUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AmlUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    amlService = TestBed.inject(AmlService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const aml: IAml = { id: 456 };

      activatedRoute.data = of({ aml });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(aml));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Aml>>();
      const aml = { id: 123 };
      jest.spyOn(amlService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aml });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aml }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(amlService.update).toHaveBeenCalledWith(aml);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Aml>>();
      const aml = new Aml();
      jest.spyOn(amlService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aml });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aml }));
      saveSubject.complete();

      // THEN
      expect(amlService.create).toHaveBeenCalledWith(aml);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Aml>>();
      const aml = { id: 123 };
      jest.spyOn(amlService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aml });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(amlService.update).toHaveBeenCalledWith(aml);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
