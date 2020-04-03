import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { INotation } from 'app/shared/model/notation.model';
import { NotationService } from './notation.service';
import { NotationDeleteDialogComponent } from './notation-delete-dialog.component';

@Component({
  selector: 'jhi-notation',
  templateUrl: './notation.component.html'
})
export class NotationComponent implements OnInit, OnDestroy {
  notations?: INotation[];
  eventSubscriber?: Subscription;

  constructor(protected notationService: NotationService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.notationService.query().subscribe((res: HttpResponse<INotation[]>) => (this.notations = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInNotations();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: INotation): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInNotations(): void {
    this.eventSubscriber = this.eventManager.subscribe('notationListModification', () => this.loadAll());
  }

  delete(notation: INotation): void {
    const modalRef = this.modalService.open(NotationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.notation = notation;
  }
}
