import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SensitiveDataTypeService } from '../service/sensitive-data-type.service';
import { ISensitiveDataType, SensitiveDataType } from '../sensitive-data-type.model';

import { SensitiveDataTypeUpdateComponent } from './sensitive-data-type-update.component';

describe('SensitiveDataType Management Update Component', () => {
  let comp: SensitiveDataTypeUpdateComponent;
  let fixture: ComponentFixture<SensitiveDataTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sensitiveDataTypeService: SensitiveDataTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SensitiveDataTypeUpdateComponent],
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
      .overrideTemplate(SensitiveDataTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SensitiveDataTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sensitiveDataTypeService = TestBed.inject(SensitiveDataTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sensitiveDataType: ISensitiveDataType = { id: 456 };

      activatedRoute.data = of({ sensitiveDataType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sensitiveDataType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SensitiveDataType>>();
      const sensitiveDataType = { id: 123 };
      jest.spyOn(sensitiveDataTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensitiveDataType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sensitiveDataType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sensitiveDataTypeService.update).toHaveBeenCalledWith(sensitiveDataType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SensitiveDataType>>();
      const sensitiveDataType = new SensitiveDataType();
      jest.spyOn(sensitiveDataTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensitiveDataType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sensitiveDataType }));
      saveSubject.complete();

      // THEN
      expect(sensitiveDataTypeService.create).toHaveBeenCalledWith(sensitiveDataType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SensitiveDataType>>();
      const sensitiveDataType = { id: 123 };
      jest.spyOn(sensitiveDataTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensitiveDataType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sensitiveDataTypeService.update).toHaveBeenCalledWith(sensitiveDataType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
