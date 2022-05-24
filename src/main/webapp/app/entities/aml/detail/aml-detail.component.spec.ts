import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AmlDetailComponent } from './aml-detail.component';

describe('Aml Management Detail Component', () => {
  let comp: AmlDetailComponent;
  let fixture: ComponentFixture<AmlDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AmlDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ aml: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AmlDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AmlDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load aml on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.aml).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
