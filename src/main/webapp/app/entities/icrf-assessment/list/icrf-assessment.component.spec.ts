import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { IcrfAssessmentService } from '../service/icrf-assessment.service';

import { IcrfAssessmentComponent } from './icrf-assessment.component';

describe('IcrfAssessment Management Component', () => {
  let comp: IcrfAssessmentComponent;
  let fixture: ComponentFixture<IcrfAssessmentComponent>;
  let service: IcrfAssessmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [IcrfAssessmentComponent],
    })
      .overrideTemplate(IcrfAssessmentComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IcrfAssessmentComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(IcrfAssessmentService);

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
    expect(comp.icrfAssessments?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
