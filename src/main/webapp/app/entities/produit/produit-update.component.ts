import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IProduit, Produit } from 'app/shared/model/produit.model';
import { ProduitService } from './produit.service';
import { INotation } from 'app/shared/model/notation.model';
import { NotationService } from 'app/entities/notation/notation.service';
import { IImage } from 'app/shared/model/image.model';
import { ImageService } from 'app/entities/image/image.service';
import { IMarque } from 'app/shared/model/marque.model';
import { MarqueService } from 'app/entities/marque/marque.service';

type SelectableEntity = INotation | IImage | IMarque;

type SelectableManyToManyEntity = INotation | IImage;

@Component({
  selector: 'jhi-produit-update',
  templateUrl: './produit-update.component.html'
})
export class ProduitUpdateComponent implements OnInit {
  isSaving = false;
  notations: INotation[] = [];
  images: IImage[] = [];
  marques: IMarque[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    prixVenteHtva: [null, [Validators.required]],
    prixVenteTvac: [null, [Validators.required]],
    prixAchatHt: [null, [Validators.required]],
    tauxTvaAchat: [null, [Validators.required]],
    tauxTva: [null, [Validators.required]],
    observation: [],
    description: [],
    notations: [],
    images: [],
    marque: []
  });

  constructor(
    protected produitService: ProduitService,
    protected notationService: NotationService,
    protected imageService: ImageService,
    protected marqueService: MarqueService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ produit }) => {
      this.updateForm(produit);

      this.notationService.query().subscribe((res: HttpResponse<INotation[]>) => (this.notations = res.body || []));

      this.imageService.query().subscribe((res: HttpResponse<IImage[]>) => (this.images = res.body || []));

      this.marqueService.query().subscribe((res: HttpResponse<IMarque[]>) => (this.marques = res.body || []));
    });
  }

  updateForm(produit: IProduit): void {
    this.editForm.patchValue({
      id: produit.id,
      nom: produit.nom,
      prixVenteHtva: produit.prixVenteHtva,
      prixVenteTvac: produit.prixVenteTvac,
      prixAchatHt: produit.prixAchatHt,
      tauxTvaAchat: produit.tauxTvaAchat,
      tauxTva: produit.tauxTva,
      observation: produit.observation,
      description: produit.description,
      notations: produit.notations,
      images: produit.images,
      marque: produit.marque
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const produit = this.createFromForm();
    if (produit.id !== undefined) {
      this.subscribeToSaveResponse(this.produitService.update(produit));
    } else {
      this.subscribeToSaveResponse(this.produitService.create(produit));
    }
  }

  private createFromForm(): IProduit {
    return {
      ...new Produit(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prixVenteHtva: this.editForm.get(['prixVenteHtva'])!.value,
      prixVenteTvac: this.editForm.get(['prixVenteTvac'])!.value,
      prixAchatHt: this.editForm.get(['prixAchatHt'])!.value,
      tauxTvaAchat: this.editForm.get(['tauxTvaAchat'])!.value,
      tauxTva: this.editForm.get(['tauxTva'])!.value,
      observation: this.editForm.get(['observation'])!.value,
      description: this.editForm.get(['description'])!.value,
      notations: this.editForm.get(['notations'])!.value,
      images: this.editForm.get(['images'])!.value,
      marque: this.editForm.get(['marque'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduit>>): void {
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

  getSelected(selectedVals: SelectableManyToManyEntity[], option: SelectableManyToManyEntity): SelectableManyToManyEntity {
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
