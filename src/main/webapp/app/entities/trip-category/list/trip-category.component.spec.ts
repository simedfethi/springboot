import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TripCategoryService } from '../service/trip-category.service';

import { TripCategoryComponent } from './trip-category.component';

describe('Component Tests', () => {
  describe('TripCategory Management Component', () => {
    let comp: TripCategoryComponent;
    let fixture: ComponentFixture<TripCategoryComponent>;
    let service: TripCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TripCategoryComponent],
      })
        .overrideTemplate(TripCategoryComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TripCategoryComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TripCategoryService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.tripCategories?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
