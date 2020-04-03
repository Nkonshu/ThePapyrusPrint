import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INotation } from 'app/shared/model/notation.model';

@Component({
  selector: 'jhi-notation-detail',
  templateUrl: './notation-detail.component.html'
})
export class NotationDetailComponent implements OnInit {
  notation: INotation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notation }) => (this.notation = notation));
  }

  previousState(): void {
    window.history.back();
  }
}
