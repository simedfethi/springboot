jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TripCategoryService } from '../service/trip-category.service';
import { ITripCategory, TripCategory } from '../trip-category.model';

import { TripCategoryUpdateComponent } from './trip-category-update.component';

describe('Component Tests', () => {
  describe('TripCategory Management Update Component', () => {
    let comp: TripCategoryUpdateComponent;
    let fixture: ComponentFixture<TripCategoryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tripCategoryService: TripCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TripCategoryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TripCategoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TripCategoryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tripCategoryService = TestBed.inject(TripCategoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const tripCategory: ITripCategory = { id: 456 };

        activatedRoute.data = of({ tripCategory });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tripCategory));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TripCategory>>();
        const tripCategory = { id: 123 };
        jest.spyOn(tripCategoryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tripCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tripCategory }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tripCategoryService.update).toHaveBeenCalledWith(tripCategory);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TripCategory>>();
        const tripCategory = new TripCategory();
        jest.spyOn(tripCategoryService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tripCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tripCategory }));
        saveSubject.complete();

        // THEN
        expect(tripCategoryService.create).toHaveBeenCalledWith(tripCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TripCategory>>();
        const tripCategory = { id: 123 };
        jest.spyOn(tripCategoryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tripCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tripCategoryService.update).toHaveBeenCalledWith(tripCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
