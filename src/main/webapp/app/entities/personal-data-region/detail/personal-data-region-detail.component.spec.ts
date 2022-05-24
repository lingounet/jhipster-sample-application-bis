import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonalDataRegionDetailComponent } from './personal-data-region-detail.component';

describe('PersonalDataRegion Management Detail Component', () => {
  let comp: PersonalDataRegionDetailComponent;
  let fixture: ComponentFixture<PersonalDataRegionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PersonalDataRegionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ personalDataRegion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PersonalDataRegionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PersonalDataRegionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load personalDataRegion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.personalDataRegion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
