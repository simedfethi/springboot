import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { GovernmentIDType } from 'app/entities/enumerations/government-id-type.model';
import { IUserGovernmentID, UserGovernmentID } from '../user-government-id.model';

import { UserGovernmentIDService } from './user-government-id.service';

describe('Service Tests', () => {
  describe('UserGovernmentID Service', () => {
    let service: UserGovernmentIDService;
    let httpMock: HttpTestingController;
    let elemDefault: IUserGovernmentID;
    let expectedResult: IUserGovernmentID | IUserGovernmentID[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(UserGovernmentIDService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        country: 'AAAAAAA',
        docType: GovernmentIDType.ID,
        docPicRectContentType: 'image/png',
        docPicRect: 'AAAAAAA',
        docPicVersContentType: 'image/png',
        docPicVers: 'AAAAAAA',
        docPicInstContentType: 'image/png',
        docPicInst: 'AAAAAAA',
        idVerified: false,
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

      it('should create a UserGovernmentID', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new UserGovernmentID()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a UserGovernmentID', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            country: 'BBBBBB',
            docType: 'BBBBBB',
            docPicRect: 'BBBBBB',
            docPicVers: 'BBBBBB',
            docPicInst: 'BBBBBB',
            idVerified: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a UserGovernmentID', () => {
        const patchObject = Object.assign(
          {
            docType: 'BBBBBB',
            docPicInst: 'BBBBBB',
          },
          new UserGovernmentID()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of UserGovernmentID', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            country: 'BBBBBB',
            docType: 'BBBBBB',
            docPicRect: 'BBBBBB',
            docPicVers: 'BBBBBB',
            docPicInst: 'BBBBBB',
            idVerified: true,
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

      it('should delete a UserGovernmentID', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addUserGovernmentIDToCollectionIfMissing', () => {
        it('should add a UserGovernmentID to an empty array', () => {
          const userGovernmentID: IUserGovernmentID = { id: 123 };
          expectedResult = service.addUserGovernmentIDToCollectionIfMissing([], userGovernmentID);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(userGovernmentID);
        });

        it('should not add a UserGovernmentID to an array that contains it', () => {
          const userGovernmentID: IUserGovernmentID = { id: 123 };
          const userGovernmentIDCollection: IUserGovernmentID[] = [
            {
              ...userGovernmentID,
            },
            { id: 456 },
          ];
          expectedResult = service.addUserGovernmentIDToCollectionIfMissing(userGovernmentIDCollection, userGovernmentID);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a UserGovernmentID to an array that doesn't contain it", () => {
          const userGovernmentID: IUserGovernmentID = { id: 123 };
          const userGovernmentIDCollection: IUserGovernmentID[] = [{ id: 456 }];
          expectedResult = service.addUserGovernmentIDToCollectionIfMissing(userGovernmentIDCollection, userGovernmentID);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(userGovernmentID);
        });

        it('should add only unique UserGovernmentID to an array', () => {
          const userGovernmentIDArray: IUserGovernmentID[] = [{ id: 123 }, { id: 456 }, { id: 937 }];
          const userGovernmentIDCollection: IUserGovernmentID[] = [{ id: 123 }];
          expectedResult = service.addUserGovernmentIDToCollectionIfMissing(userGovernmentIDCollection, ...userGovernmentIDArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const userGovernmentID: IUserGovernmentID = { id: 123 };
          const userGovernmentID2: IUserGovernmentID = { id: 456 };
          expectedResult = service.addUserGovernmentIDToCollectionIfMissing([], userGovernmentID, userGovernmentID2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(userGovernmentID);
          expect(expectedResult).toContain(userGovernmentID2);
        });

        it('should accept null and undefined values', () => {
          const userGovernmentID: IUserGovernmentID = { id: 123 };
          expectedResult = service.addUserGovernmentIDToCollectionIfMissing([], null, userGovernmentID, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(userGovernmentID);
        });

        it('should return initial array if no UserGovernmentID is added', () => {
          const userGovernmentIDCollection: IUserGovernmentID[] = [{ id: 123 }];
          expectedResult = service.addUserGovernmentIDToCollectionIfMissing(userGovernmentIDCollection, undefined, null);
          expect(expectedResult).toEqual(userGovernmentIDCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
