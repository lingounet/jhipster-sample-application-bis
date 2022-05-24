import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ApplicationTypeService } from '../service/application-type.service';

import { ApplicationTypeComponent } from './application-type.component';

describe('ApplicationType Management Component', () => {
  let comp: ApplicationTypeComponent;
  let fixture: ComponentFixture<ApplicationTypeComponent>;
  let service: ApplicationTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ApplicationTypeComponent],
    })
      .overrideTemplate(ApplicationTypeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApplicationTypeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ApplicationTypeService);

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
    expect(comp.applicationTypes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
