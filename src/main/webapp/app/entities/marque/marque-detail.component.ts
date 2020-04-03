import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMarque } from 'app/shared/model/marque.model';

@Component({
  selector: 'jhi-marque-detail',
  templateUrl: './marque-detail.component.html'
})
export class MarqueDetailComponent implements OnInit {
  marque: IMarque | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ marque }) => (this.marque = marque));
  }

  previousState(): void {
    window.history.back();
  }
}
