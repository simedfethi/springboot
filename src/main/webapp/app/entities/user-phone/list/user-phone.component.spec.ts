import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { UserPhoneService } from '../service/user-phone.service';

import { UserPhoneComponent } from './user-phone.component';

describe('Component Tests', () => {
  describe('UserPhone Management Component', () => {
    let comp: UserPhoneComponent;
    let fixture: ComponentFixture<UserPhoneComponent>;
    let service: UserPhoneService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UserPhoneComponent],
      })
        .overrideTemplate(UserPhoneComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserPhoneComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(UserPhoneService);

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
      expect(comp.userPhones?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
