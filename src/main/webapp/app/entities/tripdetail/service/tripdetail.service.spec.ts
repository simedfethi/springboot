import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITripdetail, Tripdetail } from '../tripdetail.model';

import { TripdetailService } from './tripdetail.service';

describe('Service Tests', () => {
  describe('Tripdetail Service', () => {
    let service: TripdetailService;
    let httpMock: HttpTestingController;
    let elemDefault: ITripdetail;
    let expectedResult: ITripdetail | ITripdetail[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TripdetailService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        minimumList: 0,
        maximumList: 0,
        createdDate: currentDate,
        lastupdated: currentDate,
        departureDate: currentDate,
        arrivalDate: currentDate,
        contentDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createdDate: currentDate.format(DATE_FORMAT),
            lastupdated: currentDate.format(DATE_FORMAT),
            departureDate: currentDate.format(DATE_FORMAT),
            arrivalDate: currentDate.format(DATE_FORMAT),
            contentDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Tripdetail', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdDate: currentDate.format(DATE_FORMAT),
            lastupdated: currentDate.format(DATE_FORMAT),
            departureDate: currentDate.format(DATE_FORMAT),
            arrivalDate: currentDate.format(DATE_FORMAT),
            contentDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate,
            lastupdated: currentDate,
            departureDate: currentDate,
            arrivalDate: currentDate,
            contentDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Tripdetail()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Tripdetail', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            minimumList: 1,
            maximumList: 1,
            createdDate: currentDate.format(DATE_FORMAT),
            lastupdated: currentDate.format(DATE_FORMAT),
            departureDate: currentDate.format(DATE_FORMAT),
            arrivalDate: currentDate.format(DATE_FORMAT),
            contentDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate,
            lastupdated: currentDate,
            departureDate: currentDate,
            arrivalDate: currentDate,
            contentDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Tripdetail', () => {
        const patchObject = Object.assign(
          {
            minimumList: 1,
            lastupdated: currentDate.format(DATE_FORMAT),
            arrivalDate: currentDate.format(DATE_FORMAT),
          },
          new Tripdetail()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createdDate: currentDate,
            lastupdated: currentDate,
            departureDate: currentDate,
            arrivalDate: currentDate,
            contentDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Tripdetail', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            minimumList: 1,
            maximumList: 1,
            createdDate: currentDate.format(DATE_FORMAT),
            lastupdated: currentDate.format(DATE_FORMAT),
            departureDate: currentDate.format(DATE_FORMAT),
            arrivalDate: currentDate.format(DATE_FORMAT),
            contentDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate,
            lastupdated: currentDate,
            departureDate: currentDate,
            arrivalDate: currentDate,
            contentDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Tripdetail', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTripdetailToCollectionIfMissing', () => {
        it('should add a Tripdetail to an empty array', () => {
          const tripdetail: ITripdetail = { id: 123 };
          expectedResult = service.addTripdetailToCollectionIfMissing([], tripdetail);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tripdetail);
        });

        it('should not add a Tripdetail to an array that contains it', () => {
          const tripdetail: ITripdetail = { id: 123 };
          const tripdetailCollection: ITripdetail[] = [
            {
              ...tripdetail,
            },
            { id: 456 },
          ];
          expectedResult = service.addTripdetailToCollectionIfMissing(tripdetailCollection, tripdetail);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Tripdetail to an array that doesn't contain it", () => {
          const tripdetail: ITripdetail = { id: 123 };
          const tripdetailCollection: ITripdetail[] = [{ id: 456 }];
          expectedResult = service.addTripdetailToCollectionIfMissing(tripdetailCollection, tripdetail);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tripdetail);
        });

        it('should add only unique Tripdetail to an array', () => {
          const tripdetailArray: ITripdetail[] = [{ id: 123 }, { id: 456 }, { id: 18045 }];
          const tripdetailCollection: ITripdetail[] = [{ id: 123 }];
          expectedResult = service.addTripdetailToCollectionIfMissing(tripdetailCollection, ...tripdetailArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tripdetail: ITripdetail = { id: 123 };
          const tripdetail2: ITripdetail = { id: 456 };
          expectedResult = service.addTripdetailToCollectionIfMissing([], tripdetail, tripdetail2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tripdetail);
          expect(expectedResult).toContain(tripdetail2);
        });

        it('should accept null and undefined values', () => {
          const tripdetail: ITripdetail = { id: 123 };
          expectedResult = service.addTripdetailToCollectionIfMissing([], null, tripdetail, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tripdetail);
        });

        it('should return initial array if no Tripdetail is added', () => {
          const tripdetailCollection: ITripdetail[] = [{ id: 123 }];
          expectedResult = service.addTripdetailToCollectionIfMissing(tripdetailCollection, undefined, null);
          expect(expectedResult).toEqual(tripdetailCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
