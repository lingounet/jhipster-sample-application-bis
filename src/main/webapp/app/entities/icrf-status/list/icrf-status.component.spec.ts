import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { IcrfStatusService } from '../service/icrf-status.service';

import { IcrfStatusComponent } from './icrf-status.component';

describe('IcrfStatus Management Component', () => {
  let comp: IcrfStatusComponent;
  let fixture: ComponentFixture<IcrfStatusComponent>;
  let service: IcrfStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [IcrfStatusComponent],
    })
      .overrideTemplate(IcrfStatusComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IcrfStatusComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(IcrfStatusService);

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
    expect(comp.icrfStatuses?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
