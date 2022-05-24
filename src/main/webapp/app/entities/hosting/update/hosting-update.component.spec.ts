import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HostingService } from '../service/hosting.service';
import { IHosting, Hosting } from '../hosting.model';
import { IHostingPlatform } from 'app/entities/hosting-platform/hosting-platform.model';
import { HostingPlatformService } from 'app/entities/hosting-platform/service/hosting-platform.service';
import { IIdentity } from 'app/entities/identity/identity.model';
import { IdentityService } from 'app/entities/identity/service/identity.service';

import { HostingUpdateComponent } from './hosting-update.component';

describe('Hosting Management Update Component', () => {
  let comp: HostingUpdateComponent;
  let fixture: ComponentFixture<HostingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let hostingService: HostingService;
  let hostingPlatformService: HostingPlatformService;
  let identityService: IdentityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HostingUpdateComponent],
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
      .overrideTemplate(HostingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HostingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    hostingService = TestBed.inject(HostingService);
    hostingPlatformService = TestBed.inject(HostingPlatformService);
    identityService = TestBed.inject(IdentityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call HostingPlatform query and add missing value', () => {
      const hosting: IHosting = { id: 456 };
      const hostingPlaform: IHostingPlatform = { id: 806 };
      hosting.hostingPlaform = hostingPlaform;

      const hostingPlatformCollection: IHostingPlatform[] = [{ id: 32426 }];
      jest.spyOn(hostingPlatformService, 'query').mockReturnValue(of(new HttpResponse({ body: hostingPlatformCollection })));
      const additionalHostingPlatforms = [hostingPlaform];
      const expectedCollection: IHostingPlatform[] = [...additionalHostingPlatforms, ...hostingPlatformCollection];
      jest.spyOn(hostingPlatformService, 'addHostingPlatformToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hosting });
      comp.ngOnInit();

      expect(hostingPlatformService.query).toHaveBeenCalled();
      expect(hostingPlatformService.addHostingPlatformToCollectionIfMissing).toHaveBeenCalledWith(
        hostingPlatformCollection,
        ...additionalHostingPlatforms
      );
      expect(comp.hostingPlatformsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Identity query and add missing value', () => {
      const hosting: IHosting = { id: 456 };
      const identity: IIdentity = { id: 42468 };
      hosting.identity = identity;

      const identityCollection: IIdentity[] = [{ id: 72815 }];
      jest.spyOn(identityService, 'query').mockReturnValue(of(new HttpResponse({ body: identityCollection })));
      const additionalIdentities = [identity];
      const expectedCollection: IIdentity[] = [...additionalIdentities, ...identityCollection];
      jest.spyOn(identityService, 'addIdentityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ hosting });
      comp.ngOnInit();

      expect(identityService.query).toHaveBeenCalled();
      expect(identityService.addIdentityToCollectionIfMissing).toHaveBeenCalledWith(identityCollection, ...additionalIdentities);
      expect(comp.identitiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const hosting: IHosting = { id: 456 };
      const hostingPlaform: IHostingPlatform = { id: 43251 };
      hosting.hostingPlaform = hostingPlaform;
      const identity: IIdentity = { id: 95073 };
      hosting.identity = identity;

      activatedRoute.data = of({ hosting });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(hosting));
      expect(comp.hostingPlatformsSharedCollection).toContain(hostingPlaform);
      expect(comp.identitiesSharedCollection).toContain(identity);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Hosting>>();
      const hosting = { id: 123 };
      jest.spyOn(hostingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hosting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hosting }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(hostingService.update).toHaveBeenCalledWith(hosting);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Hosting>>();
      const hosting = new Hosting();
      jest.spyOn(hostingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hosting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hosting }));
      saveSubject.complete();

      // THEN
      expect(hostingService.create).toHaveBeenCalledWith(hosting);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Hosting>>();
      const hosting = { id: 123 };
      jest.spyOn(hostingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hosting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(hostingService.update).toHaveBeenCalledWith(hosting);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackHostingPlatformById', () => {
      it('Should return tracked HostingPlatform primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackHostingPlatformById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackIdentityById', () => {
      it('Should return tracked Identity primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackIdentityById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
