import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IClient, Client } from 'app/shared/model/client.model';
import { ClientService } from './client.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

type SelectableEntity = IUser | IClient;

@Component({
  selector: 'jhi-client-update',
  templateUrl: './client-update.component.html'
})
export class ClientUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  images: IClient[] = [];
  dateNaissanceDp: any;

  editForm = this.fb.group({
    id: [],
    civilite: [null, [Validators.required]],
    nom: [null, [Validators.required]],
    prenom: [null, [Validators.required]],
    sexe: [null, [Validators.required]],
    dateNaissance: [null, [Validators.required]],
    telephone: [null, [Validators.required]],
    ville: [null, [Validators.required]],
    codePostal: [],
    pays: [null, [Validators.required]],
    quartier: [],
    email: [null, [Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')]],
    observation: [],
    user: [],
    image: []
  });

  constructor(
    protected clientService: ClientService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ client }) => {
      this.updateForm(client);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.clientService
        .query({ 'clientId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IClient[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IClient[]) => {
          if (!client.image || !client.image.id) {
            this.images = resBody;
          } else {
            this.clientService
              .find(client.image.id)
              .pipe(
                map((subRes: HttpResponse<IClient>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IClient[]) => (this.images = concatRes));
          }
        });
    });
  }

  updateForm(client: IClient): void {
    this.editForm.patchValue({
      id: client.id,
      civilite: client.civilite,
      nom: client.nom,
      prenom: client.prenom,
      sexe: client.sexe,
      dateNaissance: client.dateNaissance,
      telephone: client.telephone,
      ville: client.ville,
      codePostal: client.codePostal,
      pays: client.pays,
      quartier: client.quartier,
      email: client.email,
      observation: client.observation,
      user: client.user,
      image: client.image
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const client = this.createFromForm();
    if (client.id !== undefined) {
      this.subscribeToSaveResponse(this.clientService.update(client));
    } else {
      this.subscribeToSaveResponse(this.clientService.create(client));
    }
  }

  private createFromForm(): IClient {
    return {
      ...new Client(),
      id: this.editForm.get(['id'])!.value,
      civilite: this.editForm.get(['civilite'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      dateNaissance: this.editForm.get(['dateNaissance'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      ville: this.editForm.get(['ville'])!.value,
      codePostal: this.editForm.get(['codePostal'])!.value,
      pays: this.editForm.get(['pays'])!.value,
      quartier: this.editForm.get(['quartier'])!.value,
      email: this.editForm.get(['email'])!.value,
      observation: this.editForm.get(['observation'])!.value,
      user: this.editForm.get(['user'])!.value,
      image: this.editForm.get(['image'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClient>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
