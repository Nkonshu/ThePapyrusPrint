import { Moment } from 'moment';
import { IProduit } from 'app/shared/model/produit.model';
import { IFournisseur } from 'app/shared/model/fournisseur.model';

export interface ICommande {
  id?: number;
  date?: Moment;
  observation?: string;
  produits?: IProduit[];
  fournisseur?: IFournisseur;
}

export class Commande implements ICommande {
  constructor(
    public id?: number,
    public date?: Moment,
    public observation?: string,
    public produits?: IProduit[],
    public fournisseur?: IFournisseur
  ) {}
}
