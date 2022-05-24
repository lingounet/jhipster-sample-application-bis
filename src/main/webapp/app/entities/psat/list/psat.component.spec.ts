import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PsatService } from '../service/psat.service';

import { PsatComponent } from './psat.component';

describe('Psat Management Component', () => {
  let comp: PsatComponent;
  let fixture: ComponentFixture<PsatComponent>;
  let service: PsatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PsatComponent],
    })
      .overrideTemplate(PsatComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PsatComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PsatService);

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
    expect(comp.psats?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
