import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ThePapyrusPrintTestModule } from '../../../test.module';
import { FactureUpdateComponent } from 'app/entities/facture/facture-update.component';
import { FactureService } from 'app/entities/facture/facture.service';
import { Facture } from 'app/shared/model/facture.model';

describe('Component Tests', () => {
  describe('Facture Management Update Component', () => {
    let comp: FactureUpdateComponent;
    let fixture: ComponentFixture<FactureUpdateComponent>;
    let service: FactureService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ThePapyrusPrintTestModule],
        declarations: [FactureUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FactureUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FactureUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FactureService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Facture(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Facture();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
