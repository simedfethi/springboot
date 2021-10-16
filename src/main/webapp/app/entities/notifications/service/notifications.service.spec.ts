import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INotifications, Notifications } from '../notifications.model';

import { NotificationsService } from './notifications.service';

describe('Service Tests', () => {
  describe('Notifications Service', () => {
    let service: NotificationsService;
    let httpMock: HttpTestingController;
    let elemDefault: INotifications;
    let expectedResult: INotifications | INotifications[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(NotificationsService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        content: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Notifications', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Notifications()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Notifications', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            content: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Notifications', () => {
        const patchObject = Object.assign(
          {
            title: 'BBBBBB',
            content: 'BBBBBB',
          },
          new Notifications()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Notifications', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            content: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Notifications', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addNotificationsToCollectionIfMissing', () => {
        it('should add a Notifications to an empty array', () => {
          const notifications: INotifications = { id: 123 };
          expectedResult = service.addNotificationsToCollectionIfMissing([], notifications);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(notifications);
        });

        it('should not add a Notifications to an array that contains it', () => {
          const notifications: INotifications = { id: 123 };
          const notificationsCollection: INotifications[] = [
            {
              ...notifications,
            },
            { id: 456 },
          ];
          expectedResult = service.addNotificationsToCollectionIfMissing(notificationsCollection, notifications);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Notifications to an array that doesn't contain it", () => {
          const notifications: INotifications = { id: 123 };
          const notificationsCollection: INotifications[] = [{ id: 456 }];
          expectedResult = service.addNotificationsToCollectionIfMissing(notificationsCollection, notifications);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(notifications);
        });

        it('should add only unique Notifications to an array', () => {
          const notificationsArray: INotifications[] = [{ id: 123 }, { id: 456 }, { id: 88352 }];
          const notificationsCollection: INotifications[] = [{ id: 123 }];
          expectedResult = service.addNotificationsToCollectionIfMissing(notificationsCollection, ...notificationsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const notifications: INotifications = { id: 123 };
          const notifications2: INotifications = { id: 456 };
          expectedResult = service.addNotificationsToCollectionIfMissing([], notifications, notifications2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(notifications);
          expect(expectedResult).toContain(notifications2);
        });

        it('should accept null and undefined values', () => {
          const notifications: INotifications = { id: 123 };
          expectedResult = service.addNotificationsToCollectionIfMissing([], null, notifications, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(notifications);
        });

        it('should return initial array if no Notifications is added', () => {
          const notificationsCollection: INotifications[] = [{ id: 123 }];
          expectedResult = service.addNotificationsToCollectionIfMissing(notificationsCollection, undefined, null);
          expect(expectedResult).toEqual(notificationsCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
