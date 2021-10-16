jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { TripGroupMembersService } from '../service/trip-group-members.service';

import { TripGroupMembersDeleteDialogComponent } from './trip-group-members-delete-dialog.component';

describe('Component Tests', () => {
  describe('TripGroupMembers Management Delete Component', () => {
    let comp: TripGroupMembersDeleteDialogComponent;
    let fixture: ComponentFixture<TripGroupMembersDeleteDialogComponent>;
    let service: TripGroupMembersService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TripGroupMembersDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(TripGroupMembersDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TripGroupMembersDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TripGroupMembersService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

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
});
