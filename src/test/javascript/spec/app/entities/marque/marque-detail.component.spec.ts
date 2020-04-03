import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ThePapyrusPrintTestModule } from '../../../test.module';
import { MarqueDetailComponent } from 'app/entities/marque/marque-detail.component';
import { Marque } from 'app/shared/model/marque.model';

describe('Component Tests', () => {
  describe('Marque Management Detail Component', () => {
    let comp: MarqueDetailComponent;
    let fixture: ComponentFixture<MarqueDetailComponent>;
    const route = ({ data: of({ marque: new Marque(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ThePapyrusPrintTestModule],
        declarations: [MarqueDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MarqueDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MarqueDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load marque on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.marque).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
