import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IPoint, Point } from 'app/shared/model/point.model';
import { PointService } from './point.service';
import { IClient } from 'app/shared/model/client.model';
import { ClientService } from 'app/entities/client/client.service';

@Component({
  selector: 'jhi-point-update',
  templateUrl: './point-update.component.html'
})
export class PointUpdateComponent implements OnInit {
  isSaving = false;
  clients: IClient[] = [];

  editForm = this.fb.group({
    id: [],
    nombrePoint: [null, [Validators.required]],
    observation: [],
    client: []
  });

  constructor(
    protected pointService: PointService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ point }) => {
      this.updateForm(point);

      this.clientService
        .query({ 'pointId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IClient[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IClient[]) => {
          if (!point.client || !point.client.id) {
            this.clients = resBody;
          } else {
            this.clientService
              .find(point.client.id)
              .pipe(
                map((subRes: HttpResponse<IClient>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IClient[]) => (this.clients = concatRes));
          }
        });
    });
  }

  updateForm(point: IPoint): void {
    this.editForm.patchValue({
      id: point.id,
      nombrePoint: point.nombrePoint,
      observation: point.observation,
      client: point.client
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const point = this.createFromForm();
    if (point.id !== undefined) {
      this.subscribeToSaveResponse(this.pointService.update(point));
    } else {
      this.subscribeToSaveResponse(this.pointService.create(point));
    }
  }

  private createFromForm(): IPoint {
    return {
      ...new Point(),
      id: this.editForm.get(['id'])!.value,
      nombrePoint: this.editForm.get(['nombrePoint'])!.value,
      observation: this.editForm.get(['observation'])!.value,
      client: this.editForm.get(['client'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPoint>>): void {
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

  trackById(index: number, item: IClient): any {
    return item.id;
  }
}
