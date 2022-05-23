import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SensitiveDataTypeService } from '../service/sensitive-data-type.service';

import { SensitiveDataTypeComponent } from './sensitive-data-type.component';

describe('SensitiveDataType Management Component', () => {
  let comp: SensitiveDataTypeComponent;
  let fixture: ComponentFixture<SensitiveDataTypeComponent>;
  let service: SensitiveDataTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SensitiveDataTypeComponent],
    })
      .overrideTemplate(SensitiveDataTypeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SensitiveDataTypeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SensitiveDataTypeService);

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
    expect(comp.sensitiveDataTypes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
