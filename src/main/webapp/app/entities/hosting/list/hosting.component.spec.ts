import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { HostingService } from '../service/hosting.service';

import { HostingComponent } from './hosting.component';

describe('Hosting Management Component', () => {
  let comp: HostingComponent;
  let fixture: ComponentFixture<HostingComponent>;
  let service: HostingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [HostingComponent],
    })
      .overrideTemplate(HostingComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HostingComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(HostingService);

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
    expect(comp.hostings?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
