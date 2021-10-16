jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UserPhoneService } from '../service/user-phone.service';
import { IUserPhone, UserPhone } from '../user-phone.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { UserPhoneUpdateComponent } from './user-phone-update.component';

describe('Component Tests', () => {
  describe('UserPhone Management Update Component', () => {
    let comp: UserPhoneUpdateComponent;
    let fixture: ComponentFixture<UserPhoneUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let userPhoneService: UserPhoneService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UserPhoneUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UserPhoneUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserPhoneUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      userPhoneService = TestBed.inject(UserPhoneService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const userPhone: IUserPhone = { id: 456 };
        const internalUser: IUser = { id: 52705 };
        userPhone.internalUser = internalUser;

        const userCollection: IUser[] = [{ id: 21993 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [internalUser];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ userPhone });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const userPhone: IUserPhone = { id: 456 };
        const internalUser: IUser = { id: 21421 };
        userPhone.internalUser = internalUser;

        activatedRoute.data = of({ userPhone });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(userPhone));
        expect(comp.usersSharedCollection).toContain(internalUser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserPhone>>();
        const userPhone = { id: 123 };
        jest.spyOn(userPhoneService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userPhone });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: userPhone }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(userPhoneService.update).toHaveBeenCalledWith(userPhone);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserPhone>>();
        const userPhone = new UserPhone();
        jest.spyOn(userPhoneService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userPhone });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: userPhone }));
        saveSubject.complete();

        // THEN
        expect(userPhoneService.create).toHaveBeenCalledWith(userPhone);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UserPhone>>();
        const userPhone = { id: 123 };
        jest.spyOn(userPhoneService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ userPhone });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(userPhoneService.update).toHaveBeenCalledWith(userPhone);
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
