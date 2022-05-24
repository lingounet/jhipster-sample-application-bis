import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { IdentityService } from '../service/identity.service';

import { IdentityComponent } from './identity.component';

describe('Identity Management Component', () => {
  let comp: IdentityComponent;
  let fixture: ComponentFixture<IdentityComponent>;
  let service: IdentityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [IdentityComponent],
    })
      .overrideTemplate(IdentityComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IdentityComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(IdentityService);

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
    expect(comp.identities?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
