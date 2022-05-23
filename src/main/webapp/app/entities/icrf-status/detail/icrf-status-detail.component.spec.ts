import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IcrfStatusDetailComponent } from './icrf-status-detail.component';

describe('IcrfStatus Management Detail Component', () => {
  let comp: IcrfStatusDetailComponent;
  let fixture: ComponentFixture<IcrfStatusDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IcrfStatusDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ icrfStatus: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IcrfStatusDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IcrfStatusDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load icrfStatus on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.icrfStatus).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
