import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ThePapyrusPrintTestModule } from '../../../test.module';
import { MarqueUpdateComponent } from 'app/entities/marque/marque-update.component';
import { MarqueService } from 'app/entities/marque/marque.service';
import { Marque } from 'app/shared/model/marque.model';

describe('Component Tests', () => {
  describe('Marque Management Update Component', () => {
    let comp: MarqueUpdateComponent;
    let fixture: ComponentFixture<MarqueUpdateComponent>;
    let service: MarqueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ThePapyrusPrintTestModule],
        declarations: [MarqueUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MarqueUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MarqueUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MarqueService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Marque(123);
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
        const entity = new Marque();
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
