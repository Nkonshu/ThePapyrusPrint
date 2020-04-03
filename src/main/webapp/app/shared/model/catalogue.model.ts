import { IProduit } from 'app/shared/model/produit.model';
import { IFournisseur } from 'app/shared/model/fournisseur.model';

export interface ICatalogue {
  id?: number;
  nom?: string;
  observation?: string;
  produits?: IProduit[];
  fournisseurs?: IFournisseur[];
}

export class Catalogue implements ICatalogue {
  constructor(
    public id?: number,
    public nom?: string,
    public observation?: string,
    public produits?: IProduit[],
    public fournisseurs?: IFournisseur[]
  ) {}
}
