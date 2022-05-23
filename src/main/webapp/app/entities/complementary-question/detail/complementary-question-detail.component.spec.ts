import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ComplementaryQuestionDetailComponent } from './complementary-question-detail.component';

describe('ComplementaryQuestion Management Detail Component', () => {
  let comp: ComplementaryQuestionDetailComponent;
  let fixture: ComponentFixture<ComplementaryQuestionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ComplementaryQuestionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ complementaryQuestion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ComplementaryQuestionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ComplementaryQuestionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load complementaryQuestion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.complementaryQuestion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
