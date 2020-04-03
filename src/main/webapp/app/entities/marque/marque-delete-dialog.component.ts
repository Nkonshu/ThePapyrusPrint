import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMarque } from 'app/shared/model/marque.model';
import { MarqueService } from './marque.service';

@Component({
  templateUrl: './marque-delete-dialog.component.html'
})
export class MarqueDeleteDialogComponent {
  marque?: IMarque;

  constructor(protected marqueService: MarqueService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.marqueService.delete(id).subscribe(() => {
      this.eventManager.broadcast('marqueListModification');
      this.activeModal.close();
    });
  }
}
