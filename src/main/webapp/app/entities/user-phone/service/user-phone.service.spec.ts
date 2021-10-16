import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUserPhone, UserPhone } from '../user-phone.model';

import { UserPhoneService } from './user-phone.service';

describe('Service Tests', () => {
  describe('UserPhone Service', () => {
    let service: UserPhoneService;
    let httpMock: HttpTestingController;
    let elemDefault: IUserPhone;
    let expectedResult: IUserPhone | IUserPhone[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(UserPhoneService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        phoneNumber: 'AAAAAAA',
        verified: false,
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

      it('should create a UserPhone', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new UserPhone()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a UserPhone', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            phoneNumber: 'BBBBBB',
            verified: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a UserPhone', () => {
        const patchObject = Object.assign(
          {
            phoneNumber: 'BBBBBB',
          },
          new UserPhone()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of UserPhone', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            phoneNumber: 'BBBBBB',
            verified: true,
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

      it('should delete a UserPhone', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addUserPhoneToCollectionIfMissing', () => {
        it('should add a UserPhone to an empty array', () => {
          const userPhone: IUserPhone = { id: 123 };
          expectedResult = service.addUserPhoneToCollectionIfMissing([], userPhone);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(userPhone);
        });

        it('should not add a UserPhone to an array that contains it', () => {
          const userPhone: IUserPhone = { id: 123 };
          const userPhoneCollection: IUserPhone[] = [
            {
              ...userPhone,
            },
            { id: 456 },
          ];
          expectedResult = service.addUserPhoneToCollectionIfMissing(userPhoneCollection, userPhone);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a UserPhone to an array that doesn't contain it", () => {
          const userPhone: IUserPhone = { id: 123 };
          const userPhoneCollection: IUserPhone[] = [{ id: 456 }];
          expectedResult = service.addUserPhoneToCollectionIfMissing(userPhoneCollection, userPhone);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(userPhone);
        });

        it('should add only unique UserPhone to an array', () => {
          const userPhoneArray: IUserPhone[] = [{ id: 123 }, { id: 456 }, { id: 44667 }];
          const userPhoneCollection: IUserPhone[] = [{ id: 123 }];
          expectedResult = service.addUserPhoneToCollectionIfMissing(userPhoneCollection, ...userPhoneArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const userPhone: IUserPhone = { id: 123 };
          const userPhone2: IUserPhone = { id: 456 };
          expectedResult = service.addUserPhoneToCollectionIfMissing([], userPhone, userPhone2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(userPhone);
          expect(expectedResult).toContain(userPhone2);
        });

        it('should accept null and undefined values', () => {
          const userPhone: IUserPhone = { id: 123 };
          expectedResult = service.addUserPhoneToCollectionIfMissing([], null, userPhone, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(userPhone);
        });

        it('should return initial array if no UserPhone is added', () => {
          const userPhoneCollection: IUserPhone[] = [{ id: 123 }];
          expectedResult = service.addUserPhoneToCollectionIfMissing(userPhoneCollection, undefined, null);
          expect(expectedResult).toEqual(userPhoneCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
