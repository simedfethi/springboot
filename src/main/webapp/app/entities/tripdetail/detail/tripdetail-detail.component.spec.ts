import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TripdetailDetailComponent } from './tripdetail-detail.component';

describe('Component Tests', () => {
  describe('Tripdetail Management Detail Component', () => {
    let comp: TripdetailDetailComponent;
    let fixture: ComponentFixture<TripdetailDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TripdetailDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tripdetail: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TripdetailDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TripdetailDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tripdetail on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tripdetail).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
