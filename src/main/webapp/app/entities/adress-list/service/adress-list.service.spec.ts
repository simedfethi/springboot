import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAdressList, AdressList } from '../adress-list.model';

import { AdressListService } from './adress-list.service';

describe('Service Tests', () => {
  describe('AdressList Service', () => {
    let service: AdressListService;
    let httpMock: HttpTestingController;
    let elemDefault: IAdressList;
    let expectedResult: IAdressList | IAdressList[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AdressListService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        street: 'AAAAAAA',
        streetSuite: 'AAAAAAA',
        postalCode: 'AAAAAAA',
        state: 'AAAAAAA',
        country: 'AAAAAAA',
        latitude: 0,
        longitude: 0,
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

      it('should create a AdressList', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AdressList()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AdressList', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            street: 'BBBBBB',
            streetSuite: 'BBBBBB',
            postalCode: 'BBBBBB',
            state: 'BBBBBB',
            country: 'BBBBBB',
            latitude: 1,
            longitude: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AdressList', () => {
        const patchObject = Object.assign(
          {
            postalCode: 'BBBBBB',
            latitude: 1,
            longitude: 1,
          },
          new AdressList()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AdressList', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            street: 'BBBBBB',
            streetSuite: 'BBBBBB',
            postalCode: 'BBBBBB',
            state: 'BBBBBB',
            country: 'BBBBBB',
            latitude: 1,
            longitude: 1,
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

      it('should delete a AdressList', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAdressListToCollectionIfMissing', () => {
        it('should add a AdressList to an empty array', () => {
          const adressList: IAdressList = { id: 123 };
          expectedResult = service.addAdressListToCollectionIfMissing([], adressList);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(adressList);
        });

        it('should not add a AdressList to an array that contains it', () => {
          const adressList: IAdressList = { id: 123 };
          const adressListCollection: IAdressList[] = [
            {
              ...adressList,
            },
            { id: 456 },
          ];
          expectedResult = service.addAdressListToCollectionIfMissing(adressListCollection, adressList);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AdressList to an array that doesn't contain it", () => {
          const adressList: IAdressList = { id: 123 };
          const adressListCollection: IAdressList[] = [{ id: 456 }];
          expectedResult = service.addAdressListToCollectionIfMissing(adressListCollection, adressList);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(adressList);
        });

        it('should add only unique AdressList to an array', () => {
          const adressListArray: IAdressList[] = [{ id: 123 }, { id: 456 }, { id: 59743 }];
          const adressListCollection: IAdressList[] = [{ id: 123 }];
          expectedResult = service.addAdressListToCollectionIfMissing(adressListCollection, ...adressListArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const adressList: IAdressList = { id: 123 };
          const adressList2: IAdressList = { id: 456 };
          expectedResult = service.addAdressListToCollectionIfMissing([], adressList, adressList2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(adressList);
          expect(expectedResult).toContain(adressList2);
        });

        it('should accept null and undefined values', () => {
          const adressList: IAdressList = { id: 123 };
          expectedResult = service.addAdressListToCollectionIfMissing([], null, adressList, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(adressList);
        });

        it('should return initial array if no AdressList is added', () => {
          const adressListCollection: IAdressList[] = [{ id: 123 }];
          expectedResult = service.addAdressListToCollectionIfMissing(adressListCollection, undefined, null);
          expect(expectedResult).toEqual(adressListCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
