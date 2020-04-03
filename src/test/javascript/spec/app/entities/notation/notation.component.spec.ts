import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ThePapyrusPrintTestModule } from '../../../test.module';
import { NotationComponent } from 'app/entities/notation/notation.component';
import { NotationService } from 'app/entities/notation/notation.service';
import { Notation } from 'app/shared/model/notation.model';

describe('Component Tests', () => {
  describe('Notation Management Component', () => {
    let comp: NotationComponent;
    let fixture: ComponentFixture<NotationComponent>;
    let service: NotationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ThePapyrusPrintTestModule],
        declarations: [NotationComponent]
      })
        .overrideTemplate(NotationComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NotationComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NotationService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Notation(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.notations && comp.notations[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
