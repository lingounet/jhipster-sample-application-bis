import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HostingDetailComponent } from './hosting-detail.component';

describe('Hosting Management Detail Component', () => {
  let comp: HostingDetailComponent;
  let fixture: ComponentFixture<HostingDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HostingDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ hosting: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HostingDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HostingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load hosting on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.hosting).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
