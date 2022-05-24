import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ApplicationTypeService } from '../service/application-type.service';
import { IApplicationType, ApplicationType } from '../application-type.model';

import { ApplicationTypeUpdateComponent } from './application-type-update.component';

describe('ApplicationType Management Update Component', () => {
  let comp: ApplicationTypeUpdateComponent;
  let fixture: ComponentFixture<ApplicationTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let applicationTypeService: ApplicationTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ApplicationTypeUpdateComponent],
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
      .overrideTemplate(ApplicationTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApplicationTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    applicationTypeService = TestBed.inject(ApplicationTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const applicationType: IApplicationType = { id: 456 };

      activatedRoute.data = of({ applicationType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(applicationType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ApplicationType>>();
      const applicationType = { id: 123 };
      jest.spyOn(applicationTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicationType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: applicationType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(applicationTypeService.update).toHaveBeenCalledWith(applicationType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ApplicationType>>();
      const applicationType = new ApplicationType();
      jest.spyOn(applicationTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicationType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: applicationType }));
      saveSubject.complete();

      // THEN
      expect(applicationTypeService.create).toHaveBeenCalledWith(applicationType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ApplicationType>>();
      const applicationType = { id: 123 };
      jest.spyOn(applicationTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicationType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(applicationTypeService.update).toHaveBeenCalledWith(applicationType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
