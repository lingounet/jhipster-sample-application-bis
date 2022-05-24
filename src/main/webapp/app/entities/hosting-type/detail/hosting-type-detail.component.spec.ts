import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HostingTypeDetailComponent } from './hosting-type-detail.component';

describe('HostingType Management Detail Component', () => {
  let comp: HostingTypeDetailComponent;
  let fixture: ComponentFixture<HostingTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HostingTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ hostingType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HostingTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HostingTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load hostingType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.hostingType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
