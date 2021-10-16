jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AdressListService } from '../service/adress-list.service';
import { IAdressList, AdressList } from '../adress-list.model';

import { AdressListUpdateComponent } from './adress-list-update.component';

describe('Component Tests', () => {
  describe('AdressList Management Update Component', () => {
    let comp: AdressListUpdateComponent;
    let fixture: ComponentFixture<AdressListUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let adressListService: AdressListService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AdressListUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AdressListUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AdressListUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      adressListService = TestBed.inject(AdressListService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const adressList: IAdressList = { id: 456 };

        activatedRoute.data = of({ adressList });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(adressList));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AdressList>>();
        const adressList = { id: 123 };
        jest.spyOn(adressListService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ adressList });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: adressList }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(adressListService.update).toHaveBeenCalledWith(adressList);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AdressList>>();
        const adressList = new AdressList();
        jest.spyOn(adressListService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ adressList });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: adressList }));
        saveSubject.complete();

        // THEN
        expect(adressListService.create).toHaveBeenCalledWith(adressList);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AdressList>>();
        const adressList = { id: 123 };
        jest.spyOn(adressListService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ adressList });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(adressListService.update).toHaveBeenCalledWith(adressList);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
