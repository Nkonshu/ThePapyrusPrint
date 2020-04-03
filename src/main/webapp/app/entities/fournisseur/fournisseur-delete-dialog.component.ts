import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFournisseur } from 'app/shared/model/fournisseur.model';
import { FournisseurService } from './fournisseur.service';

@Component({
  templateUrl: './fournisseur-delete-dialog.component.html'
})
export class FournisseurDeleteDialogComponent {
  fournisseur?: IFournisseur;

  constructor(
    protected fournisseurService: FournisseurService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fournisseurService.delete(id).subscribe(() => {
      this.eventManager.broadcast('fournisseurListModification');
      this.activeModal.close();
    });
  }
}
