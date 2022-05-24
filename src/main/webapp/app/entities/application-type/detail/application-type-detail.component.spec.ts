import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApplicationTypeDetailComponent } from './application-type-detail.component';

describe('ApplicationType Management Detail Component', () => {
  let comp: ApplicationTypeDetailComponent;
  let fixture: ComponentFixture<ApplicationTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ApplicationTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ applicationType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ApplicationTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ApplicationTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load applicationType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.applicationType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
