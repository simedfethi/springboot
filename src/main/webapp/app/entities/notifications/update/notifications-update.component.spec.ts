jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NotificationsService } from '../service/notifications.service';
import { INotifications, Notifications } from '../notifications.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { NotificationsUpdateComponent } from './notifications-update.component';

describe('Component Tests', () => {
  describe('Notifications Management Update Component', () => {
    let comp: NotificationsUpdateComponent;
    let fixture: ComponentFixture<NotificationsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let notificationsService: NotificationsService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [NotificationsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(NotificationsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NotificationsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      notificationsService = TestBed.inject(NotificationsService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const notifications: INotifications = { id: 456 };
        const user: IUser = { id: 36904 };
        notifications.user = user;

        const userCollection: IUser[] = [{ id: 25917 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ notifications });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const notifications: INotifications = { id: 456 };
        const user: IUser = { id: 86036 };
        notifications.user = user;

        activatedRoute.data = of({ notifications });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(notifications));
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Notifications>>();
        const notifications = { id: 123 };
        jest.spyOn(notificationsService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ notifications });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: notifications }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(notificationsService.update).toHaveBeenCalledWith(notifications);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Notifications>>();
        const notifications = new Notifications();
        jest.spyOn(notificationsService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ notifications });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: notifications }));
        saveSubject.complete();

        // THEN
        expect(notificationsService.create).toHaveBeenCalledWith(notifications);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Notifications>>();
        const notifications = { id: 123 };
        jest.spyOn(notificationsService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ notifications });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(notificationsService.update).toHaveBeenCalledWith(notifications);
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
