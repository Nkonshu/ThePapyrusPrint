import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { INotation, Notation } from 'app/shared/model/notation.model';
import { NotationService } from './notation.service';

@Component({
  selector: 'jhi-notation-update',
  templateUrl: './notation-update.component.html'
})
export class NotationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    note: [],
    observation: []
  });

  constructor(protected notationService: NotationService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notation }) => {
      this.updateForm(notation);
    });
  }

  updateForm(notation: INotation): void {
    this.editForm.patchValue({
      id: notation.id,
      note: notation.note,
      observation: notation.observation
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notation = this.createFromForm();
    if (notation.id !== undefined) {
      this.subscribeToSaveResponse(this.notationService.update(notation));
    } else {
      this.subscribeToSaveResponse(this.notationService.create(notation));
    }
  }

  private createFromForm(): INotation {
    return {
      ...new Notation(),
      id: this.editForm.get(['id'])!.value,
      note: this.editForm.get(['note'])!.value,
      observation: this.editForm.get(['observation'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotation>>): void {
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
