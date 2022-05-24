import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ComplementaryQuestionService } from '../service/complementary-question.service';

import { ComplementaryQuestionComponent } from './complementary-question.component';

describe('ComplementaryQuestion Management Component', () => {
  let comp: ComplementaryQuestionComponent;
  let fixture: ComponentFixture<ComplementaryQuestionComponent>;
  let service: ComplementaryQuestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ComplementaryQuestionComponent],
    })
      .overrideTemplate(ComplementaryQuestionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ComplementaryQuestionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ComplementaryQuestionService);

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
    expect(comp.complementaryQuestions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
