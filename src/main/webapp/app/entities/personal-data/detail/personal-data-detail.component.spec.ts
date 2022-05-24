import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonalDataDetailComponent } from './personal-data-detail.component';

describe('PersonalData Management Detail Component', () => {
  let comp: PersonalDataDetailComponent;
  let fixture: ComponentFixture<PersonalDataDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PersonalDataDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ personalData: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PersonalDataDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PersonalDataDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load personalData on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.personalData).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
