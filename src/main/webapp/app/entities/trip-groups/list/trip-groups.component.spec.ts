import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TripGroupsService } from '../service/trip-groups.service';

import { TripGroupsComponent } from './trip-groups.component';

describe('Component Tests', () => {
  describe('TripGroups Management Component', () => {
    let comp: TripGroupsComponent;
    let fixture: ComponentFixture<TripGroupsComponent>;
    let service: TripGroupsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TripGroupsComponent],
      })
        .overrideTemplate(TripGroupsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TripGroupsComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TripGroupsService);

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
      expect(comp.tripGroups?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
