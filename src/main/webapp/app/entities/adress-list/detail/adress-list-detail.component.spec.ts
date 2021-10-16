import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AdressListDetailComponent } from './adress-list-detail.component';

describe('Component Tests', () => {
  describe('AdressList Management Detail Component', () => {
    let comp: AdressListDetailComponent;
    let fixture: ComponentFixture<AdressListDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AdressListDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ adressList: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AdressListDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AdressListDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load adressList on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.adressList).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
