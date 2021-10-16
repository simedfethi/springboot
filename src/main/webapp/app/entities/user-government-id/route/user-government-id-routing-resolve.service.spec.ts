jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IUserGovernmentID, UserGovernmentID } from '../user-government-id.model';
import { UserGovernmentIDService } from '../service/user-government-id.service';

import { UserGovernmentIDRoutingResolveService } from './user-government-id-routing-resolve.service';

describe('Service Tests', () => {
  describe('UserGovernmentID routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: UserGovernmentIDRoutingResolveService;
    let service: UserGovernmentIDService;
    let resultUserGovernmentID: IUserGovernmentID | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(UserGovernmentIDRoutingResolveService);
      service = TestBed.inject(UserGovernmentIDService);
      resultUserGovernmentID = undefined;
    });

    describe('resolve', () => {
      it('should return IUserGovernmentID returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUserGovernmentID = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUserGovernmentID).toEqual({ id: 123 });
      });

      it('should return new IUserGovernmentID if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUserGovernmentID = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultUserGovernmentID).toEqual(new UserGovernmentID());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as UserGovernmentID })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUserGovernmentID = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUserGovernmentID).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
