import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITripGroupMembers, TripGroupMembers } from '../trip-group-members.model';

import { TripGroupMembersService } from './trip-group-members.service';

describe('Service Tests', () => {
  describe('TripGroupMembers Service', () => {
    let service: TripGroupMembersService;
    let httpMock: HttpTestingController;
    let elemDefault: ITripGroupMembers;
    let expectedResult: ITripGroupMembers | ITripGroupMembers[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TripGroupMembersService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
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

      it('should create a TripGroupMembers', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TripGroupMembers()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TripGroupMembers', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TripGroupMembers', () => {
        const patchObject = Object.assign({}, new TripGroupMembers());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TripGroupMembers', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
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

      it('should delete a TripGroupMembers', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTripGroupMembersToCollectionIfMissing', () => {
        it('should add a TripGroupMembers to an empty array', () => {
          const tripGroupMembers: ITripGroupMembers = { id: 123 };
          expectedResult = service.addTripGroupMembersToCollectionIfMissing([], tripGroupMembers);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tripGroupMembers);
        });

        it('should not add a TripGroupMembers to an array that contains it', () => {
          const tripGroupMembers: ITripGroupMembers = { id: 123 };
          const tripGroupMembersCollection: ITripGroupMembers[] = [
            {
              ...tripGroupMembers,
            },
            { id: 456 },
          ];
          expectedResult = service.addTripGroupMembersToCollectionIfMissing(tripGroupMembersCollection, tripGroupMembers);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TripGroupMembers to an array that doesn't contain it", () => {
          const tripGroupMembers: ITripGroupMembers = { id: 123 };
          const tripGroupMembersCollection: ITripGroupMembers[] = [{ id: 456 }];
          expectedResult = service.addTripGroupMembersToCollectionIfMissing(tripGroupMembersCollection, tripGroupMembers);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tripGroupMembers);
        });

        it('should add only unique TripGroupMembers to an array', () => {
          const tripGroupMembersArray: ITripGroupMembers[] = [{ id: 123 }, { id: 456 }, { id: 8283 }];
          const tripGroupMembersCollection: ITripGroupMembers[] = [{ id: 123 }];
          expectedResult = service.addTripGroupMembersToCollectionIfMissing(tripGroupMembersCollection, ...tripGroupMembersArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tripGroupMembers: ITripGroupMembers = { id: 123 };
          const tripGroupMembers2: ITripGroupMembers = { id: 456 };
          expectedResult = service.addTripGroupMembersToCollectionIfMissing([], tripGroupMembers, tripGroupMembers2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tripGroupMembers);
          expect(expectedResult).toContain(tripGroupMembers2);
        });

        it('should accept null and undefined values', () => {
          const tripGroupMembers: ITripGroupMembers = { id: 123 };
          expectedResult = service.addTripGroupMembersToCollectionIfMissing([], null, tripGroupMembers, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tripGroupMembers);
        });

        it('should return initial array if no TripGroupMembers is added', () => {
          const tripGroupMembersCollection: ITripGroupMembers[] = [{ id: 123 }];
          expectedResult = service.addTripGroupMembersToCollectionIfMissing(tripGroupMembersCollection, undefined, null);
          expect(expectedResult).toEqual(tripGroupMembersCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
