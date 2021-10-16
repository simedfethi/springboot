jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TripdetailService } from '../service/tripdetail.service';
import { ITripdetail, Tripdetail } from '../tripdetail.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ITripCategory } from 'app/entities/trip-category/trip-category.model';
import { TripCategoryService } from 'app/entities/trip-category/service/trip-category.service';

import { TripdetailUpdateComponent } from './tripdetail-update.component';

describe('Component Tests', () => {
  describe('Tripdetail Management Update Component', () => {
    let comp: TripdetailUpdateComponent;
    let fixture: ComponentFixture<TripdetailUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tripdetailService: TripdetailService;
    let userService: UserService;
    let tripCategoryService: TripCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TripdetailUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TripdetailUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TripdetailUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tripdetailService = TestBed.inject(TripdetailService);
      userService = TestBed.inject(UserService);
      tripCategoryService = TestBed.inject(TripCategoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const tripdetail: ITripdetail = { id: 456 };
        const tripmaster: IUser = { id: 54636 };
        tripdetail.tripmaster = tripmaster;

        const userCollection: IUser[] = [{ id: 71640 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [tripmaster];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ tripdetail });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call TripCategory query and add missing value', () => {
        const tripdetail: ITripdetail = { id: 456 };
        const category: ITripCategory = { id: 45080 };
        tripdetail.category = category;

        const tripCategoryCollection: ITripCategory[] = [{ id: 97036 }];
        jest.spyOn(tripCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: tripCategoryCollection })));
        const additionalTripCategories = [category];
        const expectedCollection: ITripCategory[] = [...additionalTripCategories, ...tripCategoryCollection];
        jest.spyOn(tripCategoryService, 'addTripCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ tripdetail });
        comp.ngOnInit();

        expect(tripCategoryService.query).toHaveBeenCalled();
        expect(tripCategoryService.addTripCategoryToCollectionIfMissing).toHaveBeenCalledWith(
          tripCategoryCollection,
          ...additionalTripCategories
        );
        expect(comp.tripCategoriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const tripdetail: ITripdetail = { id: 456 };
        const tripmaster: IUser = { id: 19772 };
        tripdetail.tripmaster = tripmaster;
        const category: ITripCategory = { id: 63548 };
        tripdetail.category = category;

        activatedRoute.data = of({ tripdetail });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tripdetail));
        expect(comp.usersSharedCollection).toContain(tripmaster);
        expect(comp.tripCategoriesSharedCollection).toContain(category);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tripdetail>>();
        const tripdetail = { id: 123 };
        jest.spyOn(tripdetailService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tripdetail });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tripdetail }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tripdetailService.update).toHaveBeenCalledWith(tripdetail);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tripdetail>>();
        const tripdetail = new Tripdetail();
        jest.spyOn(tripdetailService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tripdetail });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tripdetail }));
        saveSubject.complete();

        // THEN
        expect(tripdetailService.create).toHaveBeenCalledWith(tripdetail);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tripdetail>>();
        const tripdetail = { id: 123 };
        jest.spyOn(tripdetailService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tripdetail });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tripdetailService.update).toHaveBeenCalledWith(tripdetail);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTripCategoryById', () => {
        it('Should return tracked TripCategory primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTripCategoryById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
