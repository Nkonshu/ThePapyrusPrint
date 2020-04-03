import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ThePapyrusPrintTestModule } from '../../../test.module';
import { NotationDetailComponent } from 'app/entities/notation/notation-detail.component';
import { Notation } from 'app/shared/model/notation.model';

describe('Component Tests', () => {
  describe('Notation Management Detail Component', () => {
    let comp: NotationDetailComponent;
    let fixture: ComponentFixture<NotationDetailComponent>;
    const route = ({ data: of({ notation: new Notation(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ThePapyrusPrintTestModule],
        declarations: [NotationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(NotationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NotationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load notation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.notation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
