import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HostingPlatformDetailComponent } from './hosting-platform-detail.component';

describe('HostingPlatform Management Detail Component', () => {
  let comp: HostingPlatformDetailComponent;
  let fixture: ComponentFixture<HostingPlatformDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HostingPlatformDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ hostingPlatform: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HostingPlatformDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HostingPlatformDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load hostingPlatform on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.hostingPlatform).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
