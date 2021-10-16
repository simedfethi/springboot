import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TripCategoryDetailComponent } from './trip-category-detail.component';

describe('Component Tests', () => {
  describe('TripCategory Management Detail Component', () => {
    let comp: TripCategoryDetailComponent;
    let fixture: ComponentFixture<TripCategoryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TripCategoryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tripCategory: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TripCategoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TripCategoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tripCategory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tripCategory).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
