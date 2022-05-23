import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SensitiveDataTypeDetailComponent } from './sensitive-data-type-detail.component';

describe('SensitiveDataType Management Detail Component', () => {
  let comp: SensitiveDataTypeDetailComponent;
  let fixture: ComponentFixture<SensitiveDataTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SensitiveDataTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sensitiveDataType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SensitiveDataTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SensitiveDataTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sensitiveDataType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sensitiveDataType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
