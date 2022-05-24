import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HostingPlatformService } from '../service/hosting-platform.service';
import { IHostingPlatform, HostingPlatform } from '../hosting-platform.model';
import { IHostingType } from 'app/entities/hosting-type/hosting-type.model';
import { HostingTypeService } from 'app/entities/hosting-type/service/hosting-type.service';

import { HostingPlatformUpdateComponent } from './hosting-platform-update.component';

describe('HostingPlatform Management Update Component', () => {
  let comp: HostingPlatformUpdateComponent;
  let fixture: ComponentFixture<HostingPlatformUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let hostingPlatformService: HostingPlatformService;
  let hostingTypeService: HostingTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HostingPlatformUpdateComponent],
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
      .overrideTemplate(HostingPlatformUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HostingPlatformUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    hostingPlatformService = TestBed.inject(HostingPlatformService);
    hostingTypeService = TestBed.inject(HostingTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call HostingType query and add missing value', () => {
      const hostingPlatform: IHostingPlatform = { id: 456 };
      const hostingType: IHostingType = { id: 88850 };
      hostingPlatform.hostingType = hostingType;

      const hostingTypeCollection: IHostingType[] = [{ id: 29657 }];
      jest.spyOn(hostingTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: hostingTypeCollection })));
      const additionalHostingTypes = [hostingType];
      const expectedCollection: IHostingType[] = [...additionalHostingTypes, ...hostingTypeCollection];
      jest.spyOn(hostingTypeService, 'addHostingTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hostingPlatform });
      comp.ngOnInit();

      expect(hostingTypeService.query).toHaveBeenCalled();
      expect(hostingTypeService.addHostingTypeToCollectionIfMissing).toHaveBeenCalledWith(hostingTypeCollection, ...additionalHostingTypes);
      expect(comp.hostingTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const hostingPlatform: IHostingPlatform = { id: 456 };
      const hostingType: IHostingType = { id: 78706 };
      hostingPlatform.hostingType = hostingType;

      activatedRoute.data = of({ hostingPlatform });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(hostingPlatform));
      expect(comp.hostingTypesSharedCollection).toContain(hostingType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<HostingPlatform>>();
      const hostingPlatform = { id: 123 };
      jest.spyOn(hostingPlatformService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hostingPlatform });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hostingPlatform }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(hostingPlatformService.update).toHaveBeenCalledWith(hostingPlatform);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<HostingPlatform>>();
      const hostingPlatform = new HostingPlatform();
      jest.spyOn(hostingPlatformService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hostingPlatform });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hostingPlatform }));
      saveSubject.complete();

      // THEN
      expect(hostingPlatformService.create).toHaveBeenCalledWith(hostingPlatform);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<HostingPlatform>>();
      const hostingPlatform = { id: 123 };
      jest.spyOn(hostingPlatformService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hostingPlatform });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(hostingPlatformService.update).toHaveBeenCalledWith(hostingPlatform);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackHostingTypeById', () => {
      it('Should return tracked HostingType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackHostingTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
