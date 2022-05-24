import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { HostingPlatformService } from '../service/hosting-platform.service';

import { HostingPlatformComponent } from './hosting-platform.component';

describe('HostingPlatform Management Component', () => {
  let comp: HostingPlatformComponent;
  let fixture: ComponentFixture<HostingPlatformComponent>;
  let service: HostingPlatformService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [HostingPlatformComponent],
    })
      .overrideTemplate(HostingPlatformComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HostingPlatformComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(HostingPlatformService);

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
    expect(comp.hostingPlatforms?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
