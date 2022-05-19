import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IcrfDetailComponent } from './icrf-detail.component';

describe('Icrf Management Detail Component', () => {
  let comp: IcrfDetailComponent;
  let fixture: ComponentFixture<IcrfDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IcrfDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ icrf: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IcrfDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IcrfDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load icrf on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.icrf).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
