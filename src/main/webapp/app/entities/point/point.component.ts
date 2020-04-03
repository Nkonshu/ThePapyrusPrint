import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPoint } from 'app/shared/model/point.model';
import { PointService } from './point.service';
import { PointDeleteDialogComponent } from './point-delete-dialog.component';

@Component({
  selector: 'jhi-point',
  templateUrl: './point.component.html'
})
export class PointComponent implements OnInit, OnDestroy {
  points?: IPoint[];
  eventSubscriber?: Subscription;

  constructor(protected pointService: PointService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.pointService.query().subscribe((res: HttpResponse<IPoint[]>) => (this.points = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPoints();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPoint): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPoints(): void {
    this.eventSubscriber = this.eventManager.subscribe('pointListModification', () => this.loadAll());
  }

  delete(point: IPoint): void {
    const modalRef = this.modalService.open(PointDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.point = point;
  }
}
