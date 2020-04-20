import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProduit } from 'app/shared/model/produit.model';

@Component({
  selector: 'jhi-produit-detail',
  templateUrl: './produit-detail.component.html',
  styleUrls: ['produit-detail.component.scss']
})
export class ProduitDetailComponent implements OnInit {
  produit: IProduit | null = null;
  images: any;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ produit }) => (this.produit = produit));
    this.getImagesDetails();
  }

  previousState(): void {
    window.history.back();
  }

  getImagesDetails(): void {
    let str = '';
    if (this.produit!.description !== undefined) str = this.produit!.description;
    this.images = str.split('--');
    this.images[0] = '../../../content/images/' + this.images[0];
    this.images[1] = '../../../content/images/' + this.images[1];
  }
}
