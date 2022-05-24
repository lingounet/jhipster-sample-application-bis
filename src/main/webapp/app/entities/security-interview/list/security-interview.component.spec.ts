import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SecurityInterviewService } from '../service/security-interview.service';

import { SecurityInterviewComponent } from './security-interview.component';

describe('SecurityInterview Management Component', () => {
  let comp: SecurityInterviewComponent;
  let fixture: ComponentFixture<SecurityInterviewComponent>;
  let service: SecurityInterviewService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SecurityInterviewComponent],
    })
      .overrideTemplate(SecurityInterviewComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SecurityInterviewComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SecurityInterviewService);

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
    expect(comp.securityInterviews?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
