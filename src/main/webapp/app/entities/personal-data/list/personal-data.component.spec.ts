import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PersonalDataService } from '../service/personal-data.service';

import { PersonalDataComponent } from './personal-data.component';

describe('PersonalData Management Component', () => {
  let comp: PersonalDataComponent;
  let fixture: ComponentFixture<PersonalDataComponent>;
  let service: PersonalDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PersonalDataComponent],
    })
      .overrideTemplate(PersonalDataComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PersonalDataComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PersonalDataService);

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
    expect(comp.personalData?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
