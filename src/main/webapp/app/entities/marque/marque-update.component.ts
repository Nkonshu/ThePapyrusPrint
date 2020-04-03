import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMarque, Marque } from 'app/shared/model/marque.model';
import { MarqueService } from './marque.service';

@Component({
  selector: 'jhi-marque-update',
  templateUrl: './marque-update.component.html'
})
export class MarqueUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    observation: []
  });

  constructor(protected marqueService: MarqueService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ marque }) => {
      this.updateForm(marque);
    });
  }

  updateForm(marque: IMarque): void {
    this.editForm.patchValue({
      id: marque.id,
      nom: marque.nom,
      observation: marque.observation
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const marque = this.createFromForm();
    if (marque.id !== undefined) {
      this.subscribeToSaveResponse(this.marqueService.update(marque));
    } else {
      this.subscribeToSaveResponse(this.marqueService.create(marque));
    }
  }

  private createFromForm(): IMarque {
    return {
      ...new Marque(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      observation: this.editForm.get(['observation'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMarque>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
