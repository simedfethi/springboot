jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TripGroupMembersService } from '../service/trip-group-members.service';
import { ITripGroupMembers, TripGroupMembers } from '../trip-group-members.model';

import { TripGroupMembersUpdateComponent } from './trip-group-members-update.component';

describe('Component Tests', () => {
  describe('TripGroupMembers Management Update Component', () => {
    let comp: TripGroupMembersUpdateComponent;
    let fixture: ComponentFixture<TripGroupMembersUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tripGroupMembersService: TripGroupMembersService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TripGroupMembersUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TripGroupMembersUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TripGroupMembersUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tripGroupMembersService = TestBed.inject(TripGroupMembersService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const tripGroupMembers: ITripGroupMembers = { id: 456 };

        activatedRoute.data = of({ tripGroupMembers });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tripGroupMembers));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TripGroupMembers>>();
        const tripGroupMembers = { id: 123 };
        jest.spyOn(tripGroupMembersService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tripGroupMembers });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tripGroupMembers }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tripGroupMembersService.update).toHaveBeenCalledWith(tripGroupMembers);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TripGroupMembers>>();
        const tripGroupMembers = new TripGroupMembers();
        jest.spyOn(tripGroupMembersService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tripGroupMembers });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tripGroupMembers }));
        saveSubject.complete();

        // THEN
        expect(tripGroupMembersService.create).toHaveBeenCalledWith(tripGroupMembers);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TripGroupMembers>>();
        const tripGroupMembers = { id: 123 };
        jest.spyOn(tripGroupMembersService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tripGroupMembers });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tripGroupMembersService.update).toHaveBeenCalledWith(tripGroupMembers);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
