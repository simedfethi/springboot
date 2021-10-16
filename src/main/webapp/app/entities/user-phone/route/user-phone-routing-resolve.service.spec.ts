jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IUserPhone, UserPhone } from '../user-phone.model';
import { UserPhoneService } from '../service/user-phone.service';

import { UserPhoneRoutingResolveService } from './user-phone-routing-resolve.service';

describe('Service Tests', () => {
  describe('UserPhone routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: UserPhoneRoutingResolveService;
    let service: UserPhoneService;
    let resultUserPhone: IUserPhone | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(UserPhoneRoutingResolveService);
      service = TestBed.inject(UserPhoneService);
      resultUserPhone = undefined;
    });

    describe('resolve', () => {
      it('should return IUserPhone returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUserPhone = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUserPhone).toEqual({ id: 123 });
      });

      it('should return new IUserPhone if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUserPhone = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultUserPhone).toEqual(new UserPhone());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as UserPhone })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUserPhone = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUserPhone).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
