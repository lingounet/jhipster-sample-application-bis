import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { HostingTypeService } from '../service/hosting-type.service';

import { HostingTypeComponent } from './hosting-type.component';

describe('HostingType Management Component', () => {
  let comp: HostingTypeComponent;
  let fixture: ComponentFixture<HostingTypeComponent>;
  let service: HostingTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [HostingTypeComponent],
    })
      .overrideTemplate(HostingTypeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HostingTypeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(HostingTypeService);

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
    expect(comp.hostingTypes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
