import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { INotation } from 'app/shared/model/notation.model';
import { NotationService } from './notation.service';

@Component({
  templateUrl: './notation-delete-dialog.component.html'
})
export class NotationDeleteDialogComponent {
  notation?: INotation;

  constructor(protected notationService: NotationService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.notationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('notationListModification');
      this.activeModal.close();
    });
  }
}
