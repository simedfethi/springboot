import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TripdetailService } from '../service/tripdetail.service';

import { TripdetailComponent } from './tripdetail.component';

describe('Component Tests', () => {
  describe('Tripdetail Management Component', () => {
    let comp: TripdetailComponent;
    let fixture: ComponentFixture<TripdetailComponent>;
    let service: TripdetailService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TripdetailComponent],
      })
        .overrideTemplate(TripdetailComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TripdetailComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TripdetailService);

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
      expect(comp.tripdetails?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
