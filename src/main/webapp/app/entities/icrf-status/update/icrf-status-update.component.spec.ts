import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IcrfStatusService } from '../service/icrf-status.service';
import { IIcrfStatus, IcrfStatus } from '../icrf-status.model';

import { IcrfStatusUpdateComponent } from './icrf-status-update.component';

describe('IcrfStatus Management Update Component', () => {
  let comp: IcrfStatusUpdateComponent;
  let fixture: ComponentFixture<IcrfStatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let icrfStatusService: IcrfStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IcrfStatusUpdateComponent],
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
      .overrideTemplate(IcrfStatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IcrfStatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    icrfStatusService = TestBed.inject(IcrfStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const icrfStatus: IIcrfStatus = { id: 456 };

      activatedRoute.data = of({ icrfStatus });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(icrfStatus));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IcrfStatus>>();
      const icrfStatus = { id: 123 };
      jest.spyOn(icrfStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ icrfStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: icrfStatus }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(icrfStatusService.update).toHaveBeenCalledWith(icrfStatus);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IcrfStatus>>();
      const icrfStatus = new IcrfStatus();
      jest.spyOn(icrfStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ icrfStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: icrfStatus }));
      saveSubject.complete();

      // THEN
      expect(icrfStatusService.create).toHaveBeenCalledWith(icrfStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IcrfStatus>>();
      const icrfStatus = { id: 123 };
      jest.spyOn(icrfStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ icrfStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(icrfStatusService.update).toHaveBeenCalledWith(icrfStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
