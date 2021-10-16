import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITripGroups, TripGroups } from '../trip-groups.model';

import { TripGroupsService } from './trip-groups.service';

describe('Service Tests', () => {
  describe('TripGroups Service', () => {
    let service: TripGroupsService;
    let httpMock: HttpTestingController;
    let elemDefault: ITripGroups;
    let expectedResult: ITripGroups | ITripGroups[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TripGroupsService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        description: 'AAAAAAA',
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

      it('should create a TripGroups', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TripGroups()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TripGroups', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TripGroups', () => {
        const patchObject = Object.assign({}, new TripGroups());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TripGroups', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            description: 'BBBBBB',
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

      it('should delete a TripGroups', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTripGroupsToCollectionIfMissing', () => {
        it('should add a TripGroups to an empty array', () => {
          const tripGroups: ITripGroups = { id: 123 };
          expectedResult = service.addTripGroupsToCollectionIfMissing([], tripGroups);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tripGroups);
        });

        it('should not add a TripGroups to an array that contains it', () => {
          const tripGroups: ITripGroups = { id: 123 };
          const tripGroupsCollection: ITripGroups[] = [
            {
              ...tripGroups,
            },
            { id: 456 },
          ];
          expectedResult = service.addTripGroupsToCollectionIfMissing(tripGroupsCollection, tripGroups);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TripGroups to an array that doesn't contain it", () => {
          const tripGroups: ITripGroups = { id: 123 };
          const tripGroupsCollection: ITripGroups[] = [{ id: 456 }];
          expectedResult = service.addTripGroupsToCollectionIfMissing(tripGroupsCollection, tripGroups);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tripGroups);
        });

        it('should add only unique TripGroups to an array', () => {
          const tripGroupsArray: ITripGroups[] = [{ id: 123 }, { id: 456 }, { id: 57464 }];
          const tripGroupsCollection: ITripGroups[] = [{ id: 123 }];
          expectedResult = service.addTripGroupsToCollectionIfMissing(tripGroupsCollection, ...tripGroupsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tripGroups: ITripGroups = { id: 123 };
          const tripGroups2: ITripGroups = { id: 456 };
          expectedResult = service.addTripGroupsToCollectionIfMissing([], tripGroups, tripGroups2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tripGroups);
          expect(expectedResult).toContain(tripGroups2);
        });

        it('should accept null and undefined values', () => {
          const tripGroups: ITripGroups = { id: 123 };
          expectedResult = service.addTripGroupsToCollectionIfMissing([], null, tripGroups, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tripGroups);
        });

        it('should return initial array if no TripGroups is added', () => {
          const tripGroupsCollection: ITripGroups[] = [{ id: 123 }];
          expectedResult = service.addTripGroupsToCollectionIfMissing(tripGroupsCollection, undefined, null);
          expect(expectedResult).toEqual(tripGroupsCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
