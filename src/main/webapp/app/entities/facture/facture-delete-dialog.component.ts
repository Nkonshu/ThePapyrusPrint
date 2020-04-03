import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFacture } from 'app/shared/model/facture.model';
import { FactureService } from './facture.service';

@Component({
  templateUrl: './facture-delete-dialog.component.html'
})
export class FactureDeleteDialogComponent {
  facture?: IFacture;

  constructor(protected factureService: FactureService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.factureService.delete(id).subscribe(() => {
      this.eventManager.broadcast('factureListModification');
      this.activeModal.close();
    });
  }
}
