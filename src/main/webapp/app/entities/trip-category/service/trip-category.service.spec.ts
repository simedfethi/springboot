import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITripCategory, TripCategory } from '../trip-category.model';

import { TripCategoryService } from './trip-category.service';

describe('Service Tests', () => {
  describe('TripCategory Service', () => {
    let service: TripCategoryService;
    let httpMock: HttpTestingController;
    let elemDefault: ITripCategory;
    let expectedResult: ITripCategory | ITripCategory[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TripCategoryService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        categoryname: 'AAAAAAA',
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

      it('should create a TripCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TripCategory()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TripCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            categoryname: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TripCategory', () => {
        const patchObject = Object.assign(
          {
            categoryname: 'BBBBBB',
          },
          new TripCategory()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TripCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            categoryname: 'BBBBBB',
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

      it('should delete a TripCategory', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTripCategoryToCollectionIfMissing', () => {
        it('should add a TripCategory to an empty array', () => {
          const tripCategory: ITripCategory = { id: 123 };
          expectedResult = service.addTripCategoryToCollectionIfMissing([], tripCategory);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tripCategory);
        });

        it('should not add a TripCategory to an array that contains it', () => {
          const tripCategory: ITripCategory = { id: 123 };
          const tripCategoryCollection: ITripCategory[] = [
            {
              ...tripCategory,
            },
            { id: 456 },
          ];
          expectedResult = service.addTripCategoryToCollectionIfMissing(tripCategoryCollection, tripCategory);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TripCategory to an array that doesn't contain it", () => {
          const tripCategory: ITripCategory = { id: 123 };
          const tripCategoryCollection: ITripCategory[] = [{ id: 456 }];
          expectedResult = service.addTripCategoryToCollectionIfMissing(tripCategoryCollection, tripCategory);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tripCategory);
        });

        it('should add only unique TripCategory to an array', () => {
          const tripCategoryArray: ITripCategory[] = [{ id: 123 }, { id: 456 }, { id: 67409 }];
          const tripCategoryCollection: ITripCategory[] = [{ id: 123 }];
          expectedResult = service.addTripCategoryToCollectionIfMissing(tripCategoryCollection, ...tripCategoryArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tripCategory: ITripCategory = { id: 123 };
          const tripCategory2: ITripCategory = { id: 456 };
          expectedResult = service.addTripCategoryToCollectionIfMissing([], tripCategory, tripCategory2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tripCategory);
          expect(expectedResult).toContain(tripCategory2);
        });

        it('should accept null and undefined values', () => {
          const tripCategory: ITripCategory = { id: 123 };
          expectedResult = service.addTripCategoryToCollectionIfMissing([], null, tripCategory, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tripCategory);
        });

        it('should return initial array if no TripCategory is added', () => {
          const tripCategoryCollection: ITripCategory[] = [{ id: 123 }];
          expectedResult = service.addTripCategoryToCollectionIfMissing(tripCategoryCollection, undefined, null);
          expect(expectedResult).toEqual(tripCategoryCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
