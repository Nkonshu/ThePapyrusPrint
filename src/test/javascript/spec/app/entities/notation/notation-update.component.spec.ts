import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ThePapyrusPrintTestModule } from '../../../test.module';
import { NotationUpdateComponent } from 'app/entities/notation/notation-update.component';
import { NotationService } from 'app/entities/notation/notation.service';
import { Notation } from 'app/shared/model/notation.model';

describe('Component Tests', () => {
  describe('Notation Management Update Component', () => {
    let comp: NotationUpdateComponent;
    let fixture: ComponentFixture<NotationUpdateComponent>;
    let service: NotationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ThePapyrusPrintTestModule],
        declarations: [NotationUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(NotationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NotationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NotationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Notation(123);
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
        const entity = new Notation();
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
