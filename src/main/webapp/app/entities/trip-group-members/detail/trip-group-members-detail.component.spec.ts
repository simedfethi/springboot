import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TripGroupMembersDetailComponent } from './trip-group-members-detail.component';

describe('Component Tests', () => {
  describe('TripGroupMembers Management Detail Component', () => {
    let comp: TripGroupMembersDetailComponent;
    let fixture: ComponentFixture<TripGroupMembersDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TripGroupMembersDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tripGroupMembers: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TripGroupMembersDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TripGroupMembersDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tripGroupMembers on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tripGroupMembers).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
