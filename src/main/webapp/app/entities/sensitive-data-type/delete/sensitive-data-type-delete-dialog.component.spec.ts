jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { SensitiveDataTypeService } from '../service/sensitive-data-type.service';

import { SensitiveDataTypeDeleteDialogComponent } from './sensitive-data-type-delete-dialog.component';

describe('SensitiveDataType Management Delete Component', () => {
  let comp: SensitiveDataTypeDeleteDialogComponent;
  let fixture: ComponentFixture<SensitiveDataTypeDeleteDialogComponent>;
  let service: SensitiveDataTypeService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SensitiveDataTypeDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(SensitiveDataTypeDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SensitiveDataTypeDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SensitiveDataTypeService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
