import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PsatDetailComponent } from './psat-detail.component';

describe('Psat Management Detail Component', () => {
  let comp: PsatDetailComponent;
  let fixture: ComponentFixture<PsatDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PsatDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ psat: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PsatDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PsatDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load psat on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.psat).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
