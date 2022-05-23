import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PersonalDataTypeService } from '../service/personal-data-type.service';

import { PersonalDataTypeComponent } from './personal-data-type.component';

describe('PersonalDataType Management Component', () => {
  let comp: PersonalDataTypeComponent;
  let fixture: ComponentFixture<PersonalDataTypeComponent>;
  let service: PersonalDataTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PersonalDataTypeComponent],
    })
      .overrideTemplate(PersonalDataTypeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PersonalDataTypeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PersonalDataTypeService);

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
    expect(comp.personalDataTypes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
