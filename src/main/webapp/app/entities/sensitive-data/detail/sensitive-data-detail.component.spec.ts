import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SensitiveDataDetailComponent } from './sensitive-data-detail.component';

describe('SensitiveData Management Detail Component', () => {
  let comp: SensitiveDataDetailComponent;
  let fixture: ComponentFixture<SensitiveDataDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SensitiveDataDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sensitiveData: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SensitiveDataDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SensitiveDataDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sensitiveData on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sensitiveData).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
