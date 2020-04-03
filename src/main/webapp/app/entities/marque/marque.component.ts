import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMarque } from 'app/shared/model/marque.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { MarqueService } from './marque.service';
import { MarqueDeleteDialogComponent } from './marque-delete-dialog.component';

@Component({
  selector: 'jhi-marque',
  templateUrl: './marque.component.html'
})
export class MarqueComponent implements OnInit, OnDestroy {
  marques: IMarque[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected marqueService: MarqueService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.marques = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.marqueService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IMarque[]>) => this.paginateMarques(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.marques = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInMarques();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMarque): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInMarques(): void {
    this.eventSubscriber = this.eventManager.subscribe('marqueListModification', () => this.reset());
  }

  delete(marque: IMarque): void {
    const modalRef = this.modalService.open(MarqueDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.marque = marque;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateMarques(data: IMarque[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.marques.push(data[i]);
      }
    }
  }
}
