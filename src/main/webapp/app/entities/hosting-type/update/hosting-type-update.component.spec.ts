import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HostingTypeService } from '../service/hosting-type.service';
import { IHostingType, HostingType } from '../hosting-type.model';

import { HostingTypeUpdateComponent } from './hosting-type-update.component';

describe('HostingType Management Update Component', () => {
  let comp: HostingTypeUpdateComponent;
  let fixture: ComponentFixture<HostingTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let hostingTypeService: HostingTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HostingTypeUpdateComponent],
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
      .overrideTemplate(HostingTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HostingTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    hostingTypeService = TestBed.inject(HostingTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const hostingType: IHostingType = { id: 456 };

      activatedRoute.data = of({ hostingType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(hostingType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<HostingType>>();
      const hostingType = { id: 123 };
      jest.spyOn(hostingTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hostingType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hostingType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(hostingTypeService.update).toHaveBeenCalledWith(hostingType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<HostingType>>();
      const hostingType = new HostingType();
      jest.spyOn(hostingTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hostingType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hostingType }));
      saveSubject.complete();

      // THEN
      expect(hostingTypeService.create).toHaveBeenCalledWith(hostingType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<HostingType>>();
      const hostingType = { id: 123 };
      jest.spyOn(hostingTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hostingType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(hostingTypeService.update).toHaveBeenCalledWith(hostingType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
