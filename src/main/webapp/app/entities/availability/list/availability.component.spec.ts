import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AvailabilityService } from '../service/availability.service';

import { AvailabilityComponent } from './availability.component';

describe('Availability Management Component', () => {
  let comp: AvailabilityComponent;
  let fixture: ComponentFixture<AvailabilityComponent>;
  let service: AvailabilityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AvailabilityComponent],
    })
      .overrideTemplate(AvailabilityComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AvailabilityComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AvailabilityService);

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
    expect(comp.availabilities?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
