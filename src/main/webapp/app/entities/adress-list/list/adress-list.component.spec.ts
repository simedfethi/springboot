import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AdressListService } from '../service/adress-list.service';

import { AdressListComponent } from './adress-list.component';

describe('Component Tests', () => {
  describe('AdressList Management Component', () => {
    let comp: AdressListComponent;
    let fixture: ComponentFixture<AdressListComponent>;
    let service: AdressListService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AdressListComponent],
      })
        .overrideTemplate(AdressListComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AdressListComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AdressListService);

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
      expect(comp.adressLists?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
