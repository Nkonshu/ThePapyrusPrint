import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFournisseur } from 'app/shared/model/fournisseur.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { FournisseurService } from './fournisseur.service';
import { FournisseurDeleteDialogComponent } from './fournisseur-delete-dialog.component';

@Component({
  selector: 'jhi-fournisseur',
  templateUrl: './fournisseur.component.html'
})
export class FournisseurComponent implements OnInit, OnDestroy {
  fournisseurs: IFournisseur[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected fournisseurService: FournisseurService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.fournisseurs = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.fournisseurService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IFournisseur[]>) => this.paginateFournisseurs(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.fournisseurs = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFournisseurs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFournisseur): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFournisseurs(): void {
    this.eventSubscriber = this.eventManager.subscribe('fournisseurListModification', () => this.reset());
  }

  delete(fournisseur: IFournisseur): void {
    const modalRef = this.modalService.open(FournisseurDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fournisseur = fournisseur;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateFournisseurs(data: IFournisseur[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.fournisseurs.push(data[i]);
      }
    }
  }
}
