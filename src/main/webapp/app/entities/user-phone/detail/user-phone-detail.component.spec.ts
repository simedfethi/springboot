import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UserPhoneDetailComponent } from './user-phone-detail.component';

describe('Component Tests', () => {
  describe('UserPhone Management Detail Component', () => {
    let comp: UserPhoneDetailComponent;
    let fixture: ComponentFixture<UserPhoneDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UserPhoneDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ userPhone: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(UserPhoneDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserPhoneDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load userPhone on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.userPhone).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
