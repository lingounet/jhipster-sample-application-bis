import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SecurityInterviewDetailComponent } from './security-interview-detail.component';

describe('SecurityInterview Management Detail Component', () => {
  let comp: SecurityInterviewDetailComponent;
  let fixture: ComponentFixture<SecurityInterviewDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SecurityInterviewDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ securityInterview: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SecurityInterviewDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SecurityInterviewDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load securityInterview on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.securityInterview).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
