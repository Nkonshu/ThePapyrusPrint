import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IStock, Stock } from 'app/shared/model/stock.model';
import { StockService } from './stock.service';
import { IProduit } from 'app/shared/model/produit.model';
import { ProduitService } from 'app/entities/produit/produit.service';

@Component({
  selector: 'jhi-stock-update',
  templateUrl: './stock-update.component.html'
})
export class StockUpdateComponent implements OnInit {
  isSaving = false;
  produits: IProduit[] = [];

  editForm = this.fb.group({
    id: [],
    stockMax: [],
    stockMin: [],
    quantiteProduit: [null, [Validators.required]],
    observation: [],
    produit: []
  });

  constructor(
    protected stockService: StockService,
    protected produitService: ProduitService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stock }) => {
      this.updateForm(stock);

      this.produitService
        .query({ 'stockId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IProduit[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IProduit[]) => {
          if (!stock.produit || !stock.produit.id) {
            this.produits = resBody;
          } else {
            this.produitService
              .find(stock.produit.id)
              .pipe(
                map((subRes: HttpResponse<IProduit>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IProduit[]) => (this.produits = concatRes));
          }
        });
    });
  }

  updateForm(stock: IStock): void {
    this.editForm.patchValue({
      id: stock.id,
      stockMax: stock.stockMax,
      stockMin: stock.stockMin,
      quantiteProduit: stock.quantiteProduit,
      observation: stock.observation,
      produit: stock.produit
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stock = this.createFromForm();
    if (stock.id !== undefined) {
      this.subscribeToSaveResponse(this.stockService.update(stock));
    } else {
      this.subscribeToSaveResponse(this.stockService.create(stock));
    }
  }

  private createFromForm(): IStock {
    return {
      ...new Stock(),
      id: this.editForm.get(['id'])!.value,
      stockMax: this.editForm.get(['stockMax'])!.value,
      stockMin: this.editForm.get(['stockMin'])!.value,
      quantiteProduit: this.editForm.get(['quantiteProduit'])!.value,
      observation: this.editForm.get(['observation'])!.value,
      produit: this.editForm.get(['produit'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStock>>): void {
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

  trackById(index: number, item: IProduit): any {
    return item.id;
  }
}
