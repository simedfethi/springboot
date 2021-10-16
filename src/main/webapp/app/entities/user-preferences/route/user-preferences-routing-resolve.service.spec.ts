jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IUserPreferences, UserPreferences } from '../user-preferences.model';
import { UserPreferencesService } from '../service/user-preferences.service';

import { UserPreferencesRoutingResolveService } from './user-preferences-routing-resolve.service';

describe('Service Tests', () => {
  describe('UserPreferences routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: UserPreferencesRoutingResolveService;
    let service: UserPreferencesService;
    let resultUserPreferences: IUserPreferences | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(UserPreferencesRoutingResolveService);
      service = TestBed.inject(UserPreferencesService);
      resultUserPreferences = undefined;
    });

    describe('resolve', () => {
      it('should return IUserPreferences returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUserPreferences = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUserPreferences).toEqual({ id: 123 });
      });

      it('should return new IUserPreferences if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUserPreferences = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultUserPreferences).toEqual(new UserPreferences());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as UserPreferences })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUserPreferences = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUserPreferences).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
