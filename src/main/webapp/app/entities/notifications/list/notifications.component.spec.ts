import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { NotificationsService } from '../service/notifications.service';

import { NotificationsComponent } from './notifications.component';

describe('Component Tests', () => {
  describe('Notifications Management Component', () => {
    let comp: NotificationsComponent;
    let fixture: ComponentFixture<NotificationsComponent>;
    let service: NotificationsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [NotificationsComponent],
      })
        .overrideTemplate(NotificationsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NotificationsComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(NotificationsService);

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
      expect(comp.notifications?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
