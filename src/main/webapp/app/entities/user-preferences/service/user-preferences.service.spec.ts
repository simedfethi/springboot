import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUserPreferences, UserPreferences } from '../user-preferences.model';

import { UserPreferencesService } from './user-preferences.service';

describe('Service Tests', () => {
  describe('UserPreferences Service', () => {
    let service: UserPreferencesService;
    let httpMock: HttpTestingController;
    let elemDefault: IUserPreferences;
    let expectedResult: IUserPreferences | IUserPreferences[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(UserPreferencesService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        language: 'AAAAAAA',
        currency: 'AAAAAAA',
        timeZone: 'AAAAAAA',
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

      it('should create a UserPreferences', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new UserPreferences()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a UserPreferences', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            language: 'BBBBBB',
            currency: 'BBBBBB',
            timeZone: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a UserPreferences', () => {
        const patchObject = Object.assign({}, new UserPreferences());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of UserPreferences', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            language: 'BBBBBB',
            currency: 'BBBBBB',
            timeZone: 'BBBBBB',
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

      it('should delete a UserPreferences', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addUserPreferencesToCollectionIfMissing', () => {
        it('should add a UserPreferences to an empty array', () => {
          const userPreferences: IUserPreferences = { id: 123 };
          expectedResult = service.addUserPreferencesToCollectionIfMissing([], userPreferences);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(userPreferences);
        });

        it('should not add a UserPreferences to an array that contains it', () => {
          const userPreferences: IUserPreferences = { id: 123 };
          const userPreferencesCollection: IUserPreferences[] = [
            {
              ...userPreferences,
            },
            { id: 456 },
          ];
          expectedResult = service.addUserPreferencesToCollectionIfMissing(userPreferencesCollection, userPreferences);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a UserPreferences to an array that doesn't contain it", () => {
          const userPreferences: IUserPreferences = { id: 123 };
          const userPreferencesCollection: IUserPreferences[] = [{ id: 456 }];
          expectedResult = service.addUserPreferencesToCollectionIfMissing(userPreferencesCollection, userPreferences);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(userPreferences);
        });

        it('should add only unique UserPreferences to an array', () => {
          const userPreferencesArray: IUserPreferences[] = [{ id: 123 }, { id: 456 }, { id: 2545 }];
          const userPreferencesCollection: IUserPreferences[] = [{ id: 123 }];
          expectedResult = service.addUserPreferencesToCollectionIfMissing(userPreferencesCollection, ...userPreferencesArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const userPreferences: IUserPreferences = { id: 123 };
          const userPreferences2: IUserPreferences = { id: 456 };
          expectedResult = service.addUserPreferencesToCollectionIfMissing([], userPreferences, userPreferences2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(userPreferences);
          expect(expectedResult).toContain(userPreferences2);
        });

        it('should accept null and undefined values', () => {
          const userPreferences: IUserPreferences = { id: 123 };
          expectedResult = service.addUserPreferencesToCollectionIfMissing([], null, userPreferences, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(userPreferences);
        });

        it('should return initial array if no UserPreferences is added', () => {
          const userPreferencesCollection: IUserPreferences[] = [{ id: 123 }];
          expectedResult = service.addUserPreferencesToCollectionIfMissing(userPreferencesCollection, undefined, null);
          expect(expectedResult).toEqual(userPreferencesCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
