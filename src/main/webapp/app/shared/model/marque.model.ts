import { IProduit } from 'app/shared/model/produit.model';

export interface IMarque {
  id?: number;
  nom?: string;
  observation?: string;
  produits?: IProduit[];
}

export class Marque implements IMarque {
  constructor(public id?: number, public nom?: string, public observation?: string, public produits?: IProduit[]) {}
}
