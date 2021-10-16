import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NotificationsDetailComponent } from './notifications-detail.component';

describe('Component Tests', () => {
  describe('Notifications Management Detail Component', () => {
    let comp: NotificationsDetailComponent;
    let fixture: ComponentFixture<NotificationsDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [NotificationsDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ notifications: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(NotificationsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NotificationsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load notifications on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.notifications).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
