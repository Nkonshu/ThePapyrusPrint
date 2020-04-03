import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICatalogue, Catalogue } from 'app/shared/model/catalogue.model';
import { CatalogueService } from './catalogue.service';
import { IProduit } from 'app/shared/model/produit.model';
import { ProduitService } from 'app/entities/produit/produit.service';
import { IFournisseur } from 'app/shared/model/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/fournisseur.service';

type SelectableEntity = IProduit | IFournisseur;

@Component({
  selector: 'jhi-catalogue-update',
  templateUrl: './catalogue-update.component.html'
})
export class CatalogueUpdateComponent implements OnInit {
  isSaving = false;
  produits: IProduit[] = [];
  fournisseurs: IFournisseur[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    observation: [],
    produits: [],
    fournisseurs: []
  });

  constructor(
    protected catalogueService: CatalogueService,
    protected produitService: ProduitService,
    protected fournisseurService: FournisseurService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogue }) => {
      this.updateForm(catalogue);

      this.produitService.query().subscribe((res: HttpResponse<IProduit[]>) => (this.produits = res.body || []));

      this.fournisseurService.query().subscribe((res: HttpResponse<IFournisseur[]>) => (this.fournisseurs = res.body || []));
    });
  }

  updateForm(catalogue: ICatalogue): void {
    this.editForm.patchValue({
      id: catalogue.id,
      nom: catalogue.nom,
      observation: catalogue.observation,
      produits: catalogue.produits,
      fournisseurs: catalogue.fournisseurs
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const catalogue = this.createFromForm();
    if (catalogue.id !== undefined) {
      this.subscribeToSaveResponse(this.catalogueService.update(catalogue));
    } else {
      this.subscribeToSaveResponse(this.catalogueService.create(catalogue));
    }
  }

  private createFromForm(): ICatalogue {
    return {
      ...new Catalogue(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      observation: this.editForm.get(['observation'])!.value,
      produits: this.editForm.get(['produits'])!.value,
      fournisseurs: this.editForm.get(['fournisseurs'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICatalogue>>): void {
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

  getSelected(selectedVals: SelectableEntity[], option: SelectableEntity): SelectableEntity {
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
