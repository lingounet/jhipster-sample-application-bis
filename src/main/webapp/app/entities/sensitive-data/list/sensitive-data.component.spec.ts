import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SensitiveDataService } from '../service/sensitive-data.service';

import { SensitiveDataComponent } from './sensitive-data.component';

describe('SensitiveData Management Component', () => {
  let comp: SensitiveDataComponent;
  let fixture: ComponentFixture<SensitiveDataComponent>;
  let service: SensitiveDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SensitiveDataComponent],
    })
      .overrideTemplate(SensitiveDataComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SensitiveDataComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SensitiveDataService);

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
    expect(comp.sensitiveData?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
