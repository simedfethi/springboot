import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { UserPreferencesService } from '../service/user-preferences.service';

import { UserPreferencesComponent } from './user-preferences.component';

describe('Component Tests', () => {
  describe('UserPreferences Management Component', () => {
    let comp: UserPreferencesComponent;
    let fixture: ComponentFixture<UserPreferencesComponent>;
    let service: UserPreferencesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UserPreferencesComponent],
      })
        .overrideTemplate(UserPreferencesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserPreferencesComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(UserPreferencesService);

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
      expect(comp.userPreferences?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
