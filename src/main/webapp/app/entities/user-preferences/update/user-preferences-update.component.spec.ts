jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UserPreferencesService } from '../service/user-preferences.service';
import { IUserPreferences, UserPreferences } from '../user-preferences.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { UserPreferencesUpdateComponent } from './user-preferences-update.component';

describe('Component Tests', () => {
  describe('UserPreferences Management Update Component', () => {
    let comp: UserPreferencesUpdateComponent;
    let fixture: ComponentFixture<UserPreferencesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let userPreferencesService: UserPreferencesService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UserPreferencesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UserPreferencesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserPreferencesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      userPreferencesService = TestBed.inject(UserPreferencesService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const userPreferences: IUserPreferences = { id: 456 };
        const internalUser: IUser = { id: 66657 };
        userPreferences.internalUser = internalUser;

        const userCollection: IUser[] = [{ id: 43285 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [internalUser];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ userPreferences });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const userPreferences: IUserPreferences = { id: 456 };
        const internalUser: IUser = { id: 20467 };
        userPreferences.internalUser = internalUser;

        activatedRoute.data = of({ userPreferences });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(userPreferences));
        expect(comp.usersSharedCollection).toContain(internalUser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserPreferences>>();
        const userPreferences = { id: 123 };
        jest.spyOn(userPreferencesService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userPreferences });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: userPreferences }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(userPreferencesService.update).toHaveBeenCalledWith(userPreferences);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserPreferences>>();
        const userPreferences = new UserPreferences();
        jest.spyOn(userPreferencesService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userPreferences });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: userPreferences }));
        saveSubject.complete();

        // THEN
        expect(userPreferencesService.create).toHaveBeenCalledWith(userPreferences);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserPreferences>>();
        const userPreferences = { id: 123 };
        jest.spyOn(userPreferencesService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userPreferences });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(userPreferencesService.update).toHaveBeenCalledWith(userPreferences);
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
