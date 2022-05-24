import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AvailabilityService } from '../service/availability.service';
import { IAvailability, Availability } from '../availability.model';

import { AvailabilityUpdateComponent } from './availability-update.component';

describe('Availability Management Update Component', () => {
  let comp: AvailabilityUpdateComponent;
  let fixture: ComponentFixture<AvailabilityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let availabilityService: AvailabilityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AvailabilityUpdateComponent],
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
      .overrideTemplate(AvailabilityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AvailabilityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    availabilityService = TestBed.inject(AvailabilityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const availability: IAvailability = { id: 456 };

      activatedRoute.data = of({ availability });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(availability));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Availability>>();
      const availability = { id: 123 };
      jest.spyOn(availabilityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ availability });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: availability }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(availabilityService.update).toHaveBeenCalledWith(availability);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Availability>>();
      const availability = new Availability();
      jest.spyOn(availabilityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ availability });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: availability }));
      saveSubject.complete();

      // THEN
      expect(availabilityService.create).toHaveBeenCalledWith(availability);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Availability>>();
      const availability = { id: 123 };
      jest.spyOn(availabilityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ availability });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(availabilityService.update).toHaveBeenCalledWith(availability);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
