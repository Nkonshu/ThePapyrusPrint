import { Component, OnInit, Input } from '@angular/core';
import { Produit, IProduit } from '../../../shared/model/produit.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ProduitDeleteDialogComponent } from '../produit-delete-dialog.component';

@Component({
  selector: 'jhi-cart-produit',
  templateUrl: './cart-produit.component.html',
  styleUrls: ['./cart-produit.component.scss']
})
export class CartProduitComponent implements OnInit {
  @Input() produit!: Produit;

  constructor(protected modalService: NgbModal) {}

  ngOnInit(): void {}

  delete(produit: IProduit): void {
    const modalRef = this.modalService.open(ProduitDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.produit = produit;
  }
}
