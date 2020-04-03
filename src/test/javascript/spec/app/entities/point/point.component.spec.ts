import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ThePapyrusPrintTestModule } from '../../../test.module';
import { PointComponent } from 'app/entities/point/point.component';
import { PointService } from 'app/entities/point/point.service';
import { Point } from 'app/shared/model/point.model';

describe('Component Tests', () => {
  describe('Point Management Component', () => {
    let comp: PointComponent;
    let fixture: ComponentFixture<PointComponent>;
    let service: PointService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ThePapyrusPrintTestModule],
        declarations: [PointComponent]
      })
        .overrideTemplate(PointComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PointComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PointService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Point(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.points && comp.points[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
