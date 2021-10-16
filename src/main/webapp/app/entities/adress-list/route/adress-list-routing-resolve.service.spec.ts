jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAdressList, AdressList } from '../adress-list.model';
import { AdressListService } from '../service/adress-list.service';

import { AdressListRoutingResolveService } from './adress-list-routing-resolve.service';

describe('Service Tests', () => {
  describe('AdressList routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AdressListRoutingResolveService;
    let service: AdressListService;
    let resultAdressList: IAdressList | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AdressListRoutingResolveService);
      service = TestBed.inject(AdressListService);
      resultAdressList = undefined;
    });

    describe('resolve', () => {
      it('should return IAdressList returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdressList = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAdressList).toEqual({ id: 123 });
      });

      it('should return new IAdressList if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdressList = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAdressList).toEqual(new AdressList());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AdressList })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdressList = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAdressList).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
