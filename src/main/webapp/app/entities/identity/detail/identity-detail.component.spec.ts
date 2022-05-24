import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IdentityDetailComponent } from './identity-detail.component';

describe('Identity Management Detail Component', () => {
  let comp: IdentityDetailComponent;
  let fixture: ComponentFixture<IdentityDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IdentityDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ identity: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IdentityDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IdentityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load identity on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.identity).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
