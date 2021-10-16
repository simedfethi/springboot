import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TripGroupsDetailComponent } from './trip-groups-detail.component';

describe('Component Tests', () => {
  describe('TripGroups Management Detail Component', () => {
    let comp: TripGroupsDetailComponent;
    let fixture: ComponentFixture<TripGroupsDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TripGroupsDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tripGroups: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TripGroupsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TripGroupsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tripGroups on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tripGroups).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
