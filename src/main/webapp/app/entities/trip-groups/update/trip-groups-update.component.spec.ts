jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TripGroupsService } from '../service/trip-groups.service';
import { ITripGroups, TripGroups } from '../trip-groups.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { TripGroupsUpdateComponent } from './trip-groups-update.component';

describe('Component Tests', () => {
  describe('TripGroups Management Update Component', () => {
    let comp: TripGroupsUpdateComponent;
    let fixture: ComponentFixture<TripGroupsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tripGroupsService: TripGroupsService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TripGroupsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TripGroupsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TripGroupsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tripGroupsService = TestBed.inject(TripGroupsService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const tripGroups: ITripGroups = { id: 456 };
        const internalUsers: IUser[] = [{ id: 44443 }];
        tripGroups.internalUsers = internalUsers;

        const userCollection: IUser[] = [{ id: 45875 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [...internalUsers];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ tripGroups });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const tripGroups: ITripGroups = { id: 456 };
        const internalUsers: IUser = { id: 84595 };
        tripGroups.internalUsers = [internalUsers];

        activatedRoute.data = of({ tripGroups });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tripGroups));
        expect(comp.usersSharedCollection).toContain(internalUsers);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TripGroups>>();
        const tripGroups = { id: 123 };
        jest.spyOn(tripGroupsService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tripGroups });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tripGroups }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tripGroupsService.update).toHaveBeenCalledWith(tripGroups);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TripGroups>>();
        const tripGroups = new TripGroups();
        jest.spyOn(tripGroupsService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tripGroups });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tripGroups }));
        saveSubject.complete();

        // THEN
        expect(tripGroupsService.create).toHaveBeenCalledWith(tripGroups);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TripGroups>>();
        const tripGroups = { id: 123 };
        jest.spyOn(tripGroupsService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tripGroups });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tripGroupsService.update).toHaveBeenCalledWith(tripGroups);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedUser', () => {
        it('Should return option if no User is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedUser(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected User for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedUser(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this User is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedUser(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
