import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IcrfAssessmentDetailComponent } from './icrf-assessment-detail.component';

describe('IcrfAssessment Management Detail Component', () => {
  let comp: IcrfAssessmentDetailComponent;
  let fixture: ComponentFixture<IcrfAssessmentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IcrfAssessmentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ icrfAssessment: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IcrfAssessmentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IcrfAssessmentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load icrfAssessment on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.icrfAssessment).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
