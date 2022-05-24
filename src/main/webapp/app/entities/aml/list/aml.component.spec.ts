import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AmlService } from '../service/aml.service';

import { AmlComponent } from './aml.component';

describe('Aml Management Component', () => {
  let comp: AmlComponent;
  let fixture: ComponentFixture<AmlComponent>;
  let service: AmlService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AmlComponent],
    })
      .overrideTemplate(AmlComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AmlComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AmlService);

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
    expect(comp.amls?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
