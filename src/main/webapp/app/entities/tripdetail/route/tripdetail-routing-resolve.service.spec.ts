jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITripdetail, Tripdetail } from '../tripdetail.model';
import { TripdetailService } from '../service/tripdetail.service';

import { TripdetailRoutingResolveService } from './tripdetail-routing-resolve.service';

describe('Service Tests', () => {
  describe('Tripdetail routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TripdetailRoutingResolveService;
    let service: TripdetailService;
    let resultTripdetail: ITripdetail | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TripdetailRoutingResolveService);
      service = TestBed.inject(TripdetailService);
      resultTripdetail = undefined;
    });

    describe('resolve', () => {
      it('should return ITripdetail returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTripdetail = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTripdetail).toEqual({ id: 123 });
      });

      it('should return new ITripdetail if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTripdetail = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTripdetail).toEqual(new Tripdetail());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Tripdetail })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTripdetail = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTripdetail).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
