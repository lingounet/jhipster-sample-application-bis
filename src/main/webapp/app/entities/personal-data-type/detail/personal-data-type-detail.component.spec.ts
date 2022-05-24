import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonalDataTypeDetailComponent } from './personal-data-type-detail.component';

describe('PersonalDataType Management Detail Component', () => {
  let comp: PersonalDataTypeDetailComponent;
  let fixture: ComponentFixture<PersonalDataTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PersonalDataTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ personalDataType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PersonalDataTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PersonalDataTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load personalDataType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.personalDataType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
