jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UserGovernmentIDService } from '../service/user-government-id.service';
import { IUserGovernmentID, UserGovernmentID } from '../user-government-id.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { UserGovernmentIDUpdateComponent } from './user-government-id-update.component';

describe('Component Tests', () => {
  describe('UserGovernmentID Management Update Component', () => {
    let comp: UserGovernmentIDUpdateComponent;
    let fixture: ComponentFixture<UserGovernmentIDUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let userGovernmentIDService: UserGovernmentIDService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UserGovernmentIDUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UserGovernmentIDUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserGovernmentIDUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      userGovernmentIDService = TestBed.inject(UserGovernmentIDService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const userGovernmentID: IUserGovernmentID = { id: 456 };
        const user: IUser = { id: 63272 };
        userGovernmentID.user = user;

        const userCollection: IUser[] = [{ id: 23785 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ userGovernmentID });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const userGovernmentID: IUserGovernmentID = { id: 456 };
        const user: IUser = { id: 49670 };
        userGovernmentID.user = user;

        activatedRoute.data = of({ userGovernmentID });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(userGovernmentID));
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserGovernmentID>>();
        const userGovernmentID = { id: 123 };
        jest.spyOn(userGovernmentIDService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userGovernmentID });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: userGovernmentID }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(userGovernmentIDService.update).toHaveBeenCalledWith(userGovernmentID);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserGovernmentID>>();
        const userGovernmentID = new UserGovernmentID();
        jest.spyOn(userGovernmentIDService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userGovernmentID });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: userGovernmentID }));
        saveSubject.complete();

        // THEN
        expect(userGovernmentIDService.create).toHaveBeenCalledWith(userGovernmentID);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserGovernmentID>>();
        const userGovernmentID = { id: 123 };
        jest.spyOn(userGovernmentIDService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userGovernmentID });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(userGovernmentIDService.update).toHaveBeenCalledWith(userGovernmentID);
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
  });
});
