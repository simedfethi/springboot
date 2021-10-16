import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TripGroupMembersService } from '../service/trip-group-members.service';

import { TripGroupMembersComponent } from './trip-group-members.component';

describe('Component Tests', () => {
  describe('TripGroupMembers Management Component', () => {
    let comp: TripGroupMembersComponent;
    let fixture: ComponentFixture<TripGroupMembersComponent>;
    let service: TripGroupMembersService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TripGroupMembersComponent],
      })
        .overrideTemplate(TripGroupMembersComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TripGroupMembersComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TripGroupMembersService);

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
      expect(comp.tripGroupMembers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
