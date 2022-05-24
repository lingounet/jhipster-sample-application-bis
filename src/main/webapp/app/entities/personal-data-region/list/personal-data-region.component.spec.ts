import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PersonalDataRegionService } from '../service/personal-data-region.service';

import { PersonalDataRegionComponent } from './personal-data-region.component';

describe('PersonalDataRegion Management Component', () => {
  let comp: PersonalDataRegionComponent;
  let fixture: ComponentFixture<PersonalDataRegionComponent>;
  let service: PersonalDataRegionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PersonalDataRegionComponent],
    })
      .overrideTemplate(PersonalDataRegionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PersonalDataRegionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PersonalDataRegionService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.personalDataRegions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
