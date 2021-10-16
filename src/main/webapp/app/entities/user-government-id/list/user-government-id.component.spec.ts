import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { UserGovernmentIDService } from '../service/user-government-id.service';

import { UserGovernmentIDComponent } from './user-government-id.component';

describe('Component Tests', () => {
  describe('UserGovernmentID Management Component', () => {
    let comp: UserGovernmentIDComponent;
    let fixture: ComponentFixture<UserGovernmentIDComponent>;
    let service: UserGovernmentIDService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UserGovernmentIDComponent],
      })
        .overrideTemplate(UserGovernmentIDComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserGovernmentIDComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(UserGovernmentIDService);

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
      expect(comp.userGovernmentIDS?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
