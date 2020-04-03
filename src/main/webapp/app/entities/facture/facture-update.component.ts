import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFacture, Facture } from 'app/shared/model/facture.model';
import { FactureService } from './facture.service';
import { IProduit } from 'app/shared/model/produit.model';
import { ProduitService } from 'app/entities/produit/produit.service';
import { IClient } from 'app/shared/model/client.model';
import { ClientService } from 'app/entities/client/client.service';

type SelectableEntity = IProduit | IClient;

@Component({
  selector: 'jhi-facture-update',
  templateUrl: './facture-update.component.html'
})
export class FactureUpdateComponent implements OnInit {
  isSaving = false;
  produits: IProduit[] = [];
  clients: IClient[] = [];
  dateDp: any;
  dateLivraisonDp: any;

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    numero: [null, [Validators.required]],
    dateLivraison: [null, [Validators.required]],
    lieuxLivraison: [null, [Validators.required]],
    orderStatus: [],
    observation: [],
    produits: [],
    client: []
  });

  constructor(
    protected factureService: FactureService,
    protected produitService: ProduitService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facture }) => {
      this.updateForm(facture);

      this.produitService.query().subscribe((res: HttpResponse<IProduit[]>) => (this.produits = res.body || []));

      this.clientService.query().subscribe((res: HttpResponse<IClient[]>) => (this.clients = res.body || []));
    });
  }

  updateForm(facture: IFacture): void {
    this.editForm.patchValue({
      id: facture.id,
      date: facture.date,
      numero: facture.numero,
      dateLivraison: facture.dateLivraison,
      lieuxLivraison: facture.lieuxLivraison,
      orderStatus: facture.orderStatus,
      observation: facture.observation,
      produits: facture.produits,
      client: facture.client
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const facture = this.createFromForm();
    if (facture.id !== undefined) {
      this.subscribeToSaveResponse(this.factureService.update(facture));
    } else {
      this.subscribeToSaveResponse(this.factureService.create(facture));
    }
  }

  private createFromForm(): IFacture {
    return {
      ...new Facture(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      dateLivraison: this.editForm.get(['dateLivraison'])!.value,
      lieuxLivraison: this.editForm.get(['lieuxLivraison'])!.value,
      orderStatus: this.editForm.get(['orderStatus'])!.value,
      observation: this.editForm.get(['observation'])!.value,
      produits: this.editForm.get(['produits'])!.value,
      client: this.editForm.get(['client'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFacture>>): void {
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

  getSelected(selectedVals: IProduit[], option: IProduit): IProduit {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
